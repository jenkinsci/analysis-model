package edu.hm.hafner.analysis.registry;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.util.ResourceTest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.assertj.core.api.Assumptions.*;

/**
 * Base class for tests of {@link IssueParser} instances.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractParserTest extends ResourceTest {
    /** Default category for parsers that do not set the category property. */
    @SuppressWarnings("resource")
    protected static final String DEFAULT_CATEGORY = new IssueBuilder().build().getCategory();

    private final String fileWithIssuesName;

    /**
     * Creates a new instance of {@link AbstractParserTest}.
     *
     * @param fileWithIssuesName
     *                           the file that should contain some issues
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
     * Parses the default file that must contain issues. Verification of the issues
     * is delegated to method {@link
     * #assertThatIssuesArePresent(Report, SoftAssertions)} that needs to be
     * implemented by subclasses.
     */
    @Test
    void shouldParseAllIssues() {
        var report = parseDefaultFile();
        try (var softAssertions = new SoftAssertions()) {
            assertThatIssuesArePresent(report, softAssertions);
        }
    }

    /**
     * Parses the default file that must contain issues. Verification of the issues
     * is delegated to method {@link
     * #assertThatIssuesArePresent(Report, SoftAssertions)} that needs to be
     * implemented by subclasses.
     */
    @Test
    void shouldRegisterParser() {
        assumeThat(createParser().getClass().getPackageName()).startsWith("edu.hm.hafner.analysis"); // only test our
                                                                                                     // own parsers

        var parserRegistry = new ParserRegistry();

        Set<Class<?>> parsers = parserRegistry.getAllDescriptors()
                .stream()
                .map(ParserDescriptor::create)
                .map(IssueParser::getClass)
                .collect(Collectors.toSet());

        var compositeParsers = parserRegistry.getAllDescriptors().stream()
                .filter(CompositeParserDescriptor.class::isInstance)
                .map(CompositeParserDescriptor.class::cast)
                .map(CompositeParserDescriptor::createParsers)
                .flatMap(Collection::stream)
                .map(IssueParser::getClass)
                .toList();
        parsers.addAll(compositeParsers);

        assertThat(parsers)
                .as("Every parser should be registered in the ParserRegistry")
                .contains(createParser().getClass());

        parserRegistry.getAllDescriptors()
                .stream()
                .map(ParserDescriptor::getUrl)
                .forEach(url -> assertThat(url).matches("https?://.*|^$"));
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
    protected Report parseDefaultFile() {
        var parser = createParser();
        var factory = getDefaultFileFactory();
        assertThat(parser.accepts(factory)).isTrue();
        return parser.parse(factory);
    }

    /**
     * Ensures that the parser under test could be serialized. This test will fail
     * with an {@code
     * NotSerializableException} if the parser does not correctly implement the
     * {@link Serializable} interface.
     */
    @Test
    void shouldBeSerializable() throws IOException {
        var parser = createParser();

        var out = new ByteArrayOutputStream();
        try (var stream = new ObjectOutputStream(out)) {
            stream.writeObject(parser);
        }

        assertThat(out.toByteArray()).isNotEmpty();
    }

    /**
     * Ensures that the parser under test can handle empty files. This test will
     * fail if the parser does not throw a
     * {@link ParsingException} or does not return an empty report.
     */
    @Test
    void shouldHandleEmptyFile() {
        boolean passed;
        try {
            var report = parseStringContent("");
            passed = report.isEmpty();
        } catch (ParsingException e) {
            passed = true;
        }
        assertThat(passed).isTrue();
    }

    /**
     * Parses the specified file and returns the found issues.
     *
     * @param fileName
     *                 the file to parse
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
     *                the log file given as String
     *
     * @return the found issues
     */
    protected Report parseStringContent(final String content) {
        return createParser().parse(new StringReaderFactory(content));
    }

    /**
     * Verifies that the provided default file has been parsed correctly. I.e., a
     * concrete test case needs to verify
     * that the number of issues is correct and that each issue contains the correct
     * properties.
     *
     * @param report
     *               the issues that have been created while parsing the default
     *               file
     * @param softly
     *               The soft assertions instance you can use for all
     *               {@link SoftAssertions#assertThat assertThat} calls. Note
     *               that {@link SoftAssertions#assertAll} is called automatically,
     *               you do not need to call it on your own.
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
     *                 the file to read
     *
     * @return default file with issues
     */
    protected ReaderFactory createReaderFactory(final String fileName) {
        return new FileReaderFactory(getResourceAsFile(fileName), StandardCharsets.UTF_8);
    }

    /** A reader factory that provides the String content (UTF_8 encoded). */
    public static class StringReaderFactory extends ReaderFactory {
        private final String content;

        /**
         * Creates a new {@link ReaderFactory} that reads the specific {@link String}
         * instance.
         *
         * @param content
         *                the content to read
         */
        public StringReaderFactory(final String content) {
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
