package edu.hm.hafner.analysis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.apache.commons.io.input.BOMInputStream;
import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static org.assertj.core.api.Assertions.*;

import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.util.ResourceTest;

/**
 * Base class for tests of {@link AbstractParser} instances.
 *
 * @param <T>
 *         subtype of created issues
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractParserTest<T extends Issue> extends ResourceTest {
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
     * Parses the default file that must contain issues. Verification of the issues is delegated to method {@link
     * #assertThatIssuesArePresent(Issues, SoftAssertions)} that needs to be implemented by sub classes.
     */
    @Test
    void shouldParseAllIssues() {
        Issues<T> issues = parseDefaultFile();
        assertSoftly(softly -> assertThatIssuesArePresent(issues, softly));
    }

    /**
     * Parses the default file that must contain issues.
     *
     * @return the issues in the default file
     */
    protected Issues<T> parseDefaultFile() {
        return createParser().parse(openFile(), Function.identity());
    }

    /**
     * Ensures that the parser under test could be serialized. This test will fail with an {@link
     * NotSerializableException} if the parser does not correctly implement the {@link Serializable} interface.
     */
    @Test
    void shouldBeSerializable() throws IOException {
        AbstractParser<T> parser = createParser();

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
    protected Issues<T> parse(final String fileName) {
        return createParser().parse(openFile(fileName), Function.identity());
    }

    /**
     * Verifies that the provided default file has been parsed correctly. I.e., a concrete test case needs to verify
     * that the number of issues is correct and that each issue contains the correct properties.
     *  @param issues
     *         the issues that have been created while parsing the default file
     * @param softly
     *         The soft assertions instance you can use for all {@link SoftAssertions#assertThat assertThat} calls. Note
     *         that {@link SoftAssertions#assertAll} is called automatically, you do not need to call it on your own.
     */
    protected abstract void assertThatIssuesArePresent(Issues<T> issues, SoftAssertions softly);

    /**
     * Creates the parser under test.
     *
     * @return the new parser instance
     */
    protected abstract AbstractParser<T> createParser();

    /**
     * Returns an input stream for the default file.
     *
     * @return an input stream
     */
    protected Reader openFile() {
        return openFile(fileWithIssuesName);
    }

    /**
     * Returns an input stream for the specified resource. The file name  must be relative to this {@link
     * AbstractParserTest} class.
     *
     * @param fileName
     *         the file to read (relative this {@link AbstractParserTest} class
     *
     * @return an {@link BOMInputStream input stream} using character set UTF-8
     */
    protected Reader openFile(final String fileName) {
        return asReader(asInputStream(fileName));
    }

    private Reader asReader(final InputStream stream) {
        return new InputStreamReader(new BOMInputStream(stream), StandardCharsets.UTF_8);
    }
}
