package edu.hm.hafner.analysis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.input.BOMInputStream;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import edu.hm.hafner.util.Ensure;
import static org.assertj.core.api.Assertions.*;

/**
 * Base class for tests of {@link AbstractParser} instances.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractParserTest {
    private final String fileWithIssuesName;

    /**
     * Creates a new instance of {@link AbstractParserTest}.
     *
     * @param fileWithIssuesName
     *         the file that should contain some issues
     */
    protected AbstractParserTest(final String fileWithIssuesName) {
        this.fileWithIssuesName = fileWithIssuesName;
    }

    /**
     * Parses the default file that must contain issues. Verification of the issues is delegated to method {@link
     * #assertThatIssuesArePresent(Issues, SoftAssertions)} that needs to be implemented by sub classes.
     */
    @Test
    void shouldParseAllIssues() {
        Issues<Issue> issues = createParser().parse(openFile());

        assertSoftly(softly -> assertThatIssuesArePresent(issues, softly));
    }

    /**
     * Ensures that the parser under test could be serialized. This test will fail with an {@link
     * NotSerializableException} if the parser does not correctly implement the {@link Serializable} interface.
     */
    @Test
    void shouldBeSerializable() throws IOException {
        AbstractParser parser = createParser();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ObjectOutputStream stream = new ObjectOutputStream(out)) {
            stream.writeObject(parser);
            stream.close();
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
    protected Issues<Issue> parse(final String fileName) {
        return createParser().parse(openFile(fileName));
    }

    /**
     * Verifies that the provided default file has been parsed correctly. I.e., a concrete test case needs to verify
     * that the number of issues is correct and that each issue contains the correct properties.
     *
     * @param issues
     *         the issues that have been created while parsing the default file
     * @param softly
     *         The soft assertions instance you can use for all {@link SoftAssertions#assertThat assertThat} calls. Note
     *         that {@link SoftAssertions#assertAll} is called automatically, you do not need to call it on your own.
     */
    protected abstract void assertThatIssuesArePresent(Issues<Issue> issues, SoftAssertions softly);

    /**
     * Creates the parser under test.
     *
     * @return the new parser instance
     */
    protected abstract AbstractParser createParser();

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
        return asReader(asStream(fileName));
    }

    private Reader asReader(final InputStream stream) {
        try {
            return new InputStreamReader(new BOMInputStream(stream), "UTF-8");
        }
        catch (UnsupportedEncodingException ignored) {
            return new InputStreamReader(stream);
        }
    }

    private InputStream asStream(final String fileName) {
        InputStream resourceAsStream = getClass().getResourceAsStream(fileName);

        Ensure.that(resourceAsStream).isNotNull("File %s not found!", fileName);

        return resourceAsStream;
    }
}
