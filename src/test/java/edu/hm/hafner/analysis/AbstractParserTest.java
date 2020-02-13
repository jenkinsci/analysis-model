package edu.hm.hafner.analysis;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Base class for tests of {@link IssueParser} instances.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractParserTest extends ResourceTest {
    /** Default category for parsers that do not set the category property. */
    protected static final String DEFAULT_CATEGORY = new IssueBuilder().build().getCategory();

    private final String fileWithIssuesName;

    /**
     * Creates a new instance of {@link AbstractParserTest}.
     *
     * @param fileWithIssuesName
     *         the file that should contain some issues
     */
    protected AbstractParserTest(final String fileWithIssuesName) {
        super();

        this.fileWithIssuesName = fileWithIssuesName;
    }

    /**
     * Returns the name of the file that should contain some issues.
     *
     * @return the file name
     */
    protected String getFileWithIssuesName() {
        return fileWithIssuesName;
    }

    /**
     * Parses the default file that must contain issues. Verification of the issues is delegated to method {@link
     * #assertThatIssuesArePresent(Report, SoftAssertions)} that needs to be implemented by sub classes.
     */
    @Test
    void shouldParseAllIssues() {
        Report report = parseDefaultFile();
        try (SoftAssertions softAssertions = new SoftAssertions()) {
            assertThatIssuesArePresent(report, softAssertions);
        }
    }

    protected void assertThatReportHasSeverities(final Report report, final int expectedSizeError,
            final int expectedSizeHigh, final int expectedSizeNormal, final int expectedSizeLow) {
        assertThat(report.getSizeOf(Severity.ERROR)).isEqualTo(expectedSizeError);
        assertThat(report.getSizeOf(Severity.WARNING_HIGH)).isEqualTo(expectedSizeHigh);
        assertThat(report.getSizeOf(Severity.WARNING_NORMAL)).isEqualTo(expectedSizeNormal);
        assertThat(report.getSizeOf(Severity.WARNING_LOW)).isEqualTo(expectedSizeLow);
    }

    /**
     * Parses the default file that must contain issues.
     *
     * @return the issues in the default file
     */
    protected Report    parseDefaultFile() {
        return createParser().parse(getDefaultFileFactory());
    }

    /**
     * Ensures that the parser under test could be serialized. This test will fail with an {@link
     * NotSerializableException} if the parser does not correctly implement the {@link Serializable} interface.
     */
    @Test
    void shouldBeSerializable() throws IOException {
        IssueParser parser = createParser();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ObjectOutputStream stream = new ObjectOutputStream(out)) {
            stream.writeObject(parser);
        }

        assertThat(out.toByteArray()).isNotEmpty();
    }

    /**
     * Parses the specified file and returns the found issues.
     *
     * @param fileName
     *         the file to parse
     *
     * @return the found issues
     */
    protected Report parse(final String fileName) {
        return createParser().parse(createReaderFactory(fileName));
    }

    /**
     * Parses the specified string content and returns the found issues.
     *
     * @param content
     *         the log file given as String
     *
     * @return the found issues
     */
    protected Report parseStringContent(final String content) {
        return createParser().parse(new StringReaderFactory(content));
    }

    /**
     * Verifies that the provided default file has been parsed correctly. I.e., a concrete test case needs to verify
     * that the number of issues is correct and that each issue contains the correct properties.
     *
     * @param report
     *         the issues that have been created while parsing the default file
     * @param softly
     *         The soft assertions instance you can use for all {@link SoftAssertions#assertThat assertThat} calls. Note
     *         that {@link SoftAssertions#assertAll} is called automatically, you do not need to call it on your own.
     */
    protected abstract void assertThatIssuesArePresent(Report report, SoftAssertions softly);

    /**
     * Creates the parser under test.
     *
     * @return the new parser instance
     */
    protected abstract IssueParser createParser();

    /**
     * Returns a factory that opens the default {@link File} on every invocation.
     *
     * @return default file with issues
     */
    ReaderFactory getDefaultFileFactory() {
        return createReaderFactory(fileWithIssuesName);
    }

    /**
     * Returns a factory that opens the specified {@link File} on every invocation.
     *
     * @param fileName
     *         the file to read
     *
     * @return default file with issues
     */
    protected ReaderFactory createReaderFactory(final String fileName) {
        return new FileReaderFactory(getResourceAsFile(fileName), StandardCharsets.UTF_8);
    }

    /** A reader factory that provides the String content. */
    private static class StringReaderFactory extends ReaderFactory {
        private final String content;

        StringReaderFactory(final String content) {
            super(StandardCharsets.UTF_8);

            this.content = content;
        }

        @Override
        public String getFileName() {
            return "String";
        }

        @Override
        public Reader create() {
            return new StringReader(content);
        }
    }
}
