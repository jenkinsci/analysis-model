package edu.hm.hafner.analysis.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.input.BOMInputStream;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.util.Ensure;
import static org.junit.Assert.*;

/**
 * Base class for parser tests. Provides an assertion test for warnings.
 * <p>
 * FIXME: close files
 */
public abstract class ParserTester {
    public static final String WRONG_NUMBER_OF_WARNINGS_DETECTED = "Wrong number of warnings detected: ";
    public static final String DEFAULT_CATEGORY = new IssueBuilder().build().getCategory();

    /**
     * Checks the properties of the specified warning.
     *
     * @param warning    the warning to check
     * @param lineNumber the expected line number
     * @param message    the expected message
     * @param fileName   the expected filename
     * @param category   the expected category
     * @param priority   the expected priority
     */
    protected void checkWarning(final Issue warning, final int lineNumber, final String message, final String fileName, final String category, final Priority priority) {
        assertEquals("Wrong priority detected: ", priority, warning.getPriority());
        assertEquals("Wrong category of warning detected: ", category, warning.getCategory());
        assertEquals("Wrong line start detected: ", lineNumber, warning.getLineStart());
        assertEquals("Wrong line end detected: ", lineNumber, warning.getLineEnd());
        assertEquals("Wrong message detected: ", message, warning.getMessage());
        assertEquals("Wrong filename detected: ", fileName, warning.getFileName());
    }

    /**
     * Checks the properties of the specified warning.
     *
     * @param warning    the warning to check
     * @param lineNumber the expected line number
     * @param column     the expected column
     * @param message    the expected message
     * @param fileName   the expected filename
     * @param category   the expected category
     * @param priority   the expected priority
     */
    protected void checkWarning(final Issue warning, final int lineNumber, final int column, final String message, final String fileName, final String category, final Priority priority) {
        checkWarning(warning, lineNumber, message, fileName, category, priority);
        assertEquals("Wrong column start detected: ", column, warning.getColumnStart());
    }

    /**
     * Checks the properties of the specified warning.
     *
     * @param warning    the warning to check
     * @param lineNumber the expected line number
     * @param message    the expected message
     * @param fileName   the expected filename
     * @param type       the expected type
     * @param category   the expected category
     * @param priority   the expected priority
     */
    protected void checkWarning(final Issue warning, final int lineNumber, final String message, final String fileName, final String type, final String category, final Priority priority) {
        checkWarning(warning, lineNumber, message, fileName, category, priority);
        assertEquals("Wrong type of warning detected: ", type, warning.getType());
    }

    /**
     * Checks the properties of the specified warning.
     *
     * @param warning    the warning to check
     * @param lineNumber the expected line number
     * @param column     the expected column
     * @param message    the expected message
     * @param fileName   the expected filename
     * @param type       the expected type
     * @param category   the expected category
     * @param priority   the expected priority
     */
    protected void checkWarning(final Issue warning, final int lineNumber, final int column, final String message, final String fileName, final String type, final String category, final Priority priority) { // NOCHECKSTYLE
        checkWarning(warning, lineNumber, column, message, fileName, category, priority);
        assertEquals("Wrong type of warning detected: ", type, warning.getType());
    }

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
     * @param fileName the file to read
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
