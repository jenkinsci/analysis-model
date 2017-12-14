package edu.hm.hafner.analysis.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.input.BOMInputStream;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.util.Ensure;

/**
 * Base class for parser tests. Provides an assertion test for warnings.
 * <p>
 * FIXME: close files
 */
public abstract class ParserTester {
    public static final String WRONG_NUMBER_OF_WARNINGS_DETECTED = "Wrong number of warnings detected: ";
    public static final String DEFAULT_CATEGORY = new IssueBuilder().build().getCategory();

    /**
     * Returns an input stream with the warnings.
     *
     * @return an input stream
     */
    protected Reader openFile() {
        return openFile(getWarningsFile());
    }

    /**
     * Returns an input stream with the warnings.
     *
     * @param fileName
     *         the file to read
     *
     * @return an input stream
     */
    protected Reader openFile(final String fileName) {
        try {
            return new InputStreamReader(new BOMInputStream(asStream(fileName)), "UTF-8");
        }
        catch (UnsupportedEncodingException exception) {
            return new InputStreamReader(asStream(fileName));
        }
    }

    private InputStream asStream(final String fileName) {
        InputStream resourceAsStream = ParserTester.class.getResourceAsStream(fileName);

        Ensure.that(resourceAsStream).isNotNull("File %s not found!", fileName);

        return resourceAsStream;
    }

    /**
     * Returns the file name of the warnings file.
     *
     * @return the warnings file name
     */
    protected abstract String getWarningsFile();
}
