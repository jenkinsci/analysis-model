package edu.hm.hafner.analysis.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.input.BOMInputStream;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Base class for parser tests. Provides an assertion test for warnings.
 *
 * FIXME: close files
 */
@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public abstract class ParserTester {
    public static final String WRONG_NUMBER_OF_WARNINGS_DETECTED = "Wrong number of warnings detected: ";
    public static final String DEFAULT_CATEGORY = new IssueBuilder().build().getCategory();

    /**
     * Checks the properties of the specified warning.
     *
     * @param warning       the warning to check
     * @param fileName      the expected filename
     * @param category      the expected category
     * @param type          the expected type
     * @param priority      the expected priority
     * @param message       the expected message
     * @param description   the expected description
     * @param packageName   the expected packageName
     * @param lineStart     the expected lineStart
     * @param lineEnd       the expected lineEnd
     * @param columnStart   the expected columnStart
     * @param columnEnd     the expected columnEnd
     */
    protected void checkWarning(final Issue warning, final String fileName, final String category,
                                final String type, final Priority priority, final String message,
                                final String description, final String packageName,
                                final int lineStart, final int lineEnd, final int columnStart, final int columnEnd) {
        assertThat(warning)
                .hasFileName(fileName)
                .hasCategory(category)
                .hasType(type)
                .hasPriority(priority)
                .hasMessage(message)
                .hasDescription(description)
                .hasPackageName(packageName)
                .hasLineStart(lineStart)
                .hasLineEnd(lineEnd)
                .hasColumnStart(columnStart)
                .hasColumnEnd(columnEnd);
    }

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
    protected void checkWarning(final Issue warning, final int lineNumber, final String message,
                                final String fileName, final String category, final Priority priority) {
        assertThat(warning)
                .hasFileName(fileName)
                .hasCategory(category)
                .hasPriority(priority)
                .hasLineStart(lineNumber) // why is lineStart always expected to be equal to lineEnd in this method?
                .hasLineEnd(lineNumber)
                .hasMessage(message);
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
        assertThat(warning)
                .hasColumnStart(column);
    }

    /**
     * Checks the properties of the specified warning.
     *
     * @param warning the warning to check
     * @param lineNumber the expected line number
     * @param message    the expected message
     * @param fileName   the expected filename
     * @param type       the expected type
     * @param category   the expected category
     * @param priority   the expected priority
     */
    protected void checkWarning(final Issue warning, final int lineNumber, final String message, final String fileName, final String type, final String category, final Priority priority) {
        checkWarning(warning, lineNumber, message, fileName, category, priority);
        assertThat(warning)
                .hasType(type);
    }

    /**
     * Checks the properties of the specified warning.
     *
     * @param warning the warning to check
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
        assertThat(warning)
                .hasType(type);
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

        assertThat(resourceAsStream)
                .as(String.format("File %s not found!", fileName))
                .isNotNull();

        return resourceAsStream;
    }

    /**
     * Returns the file name of the warnings file.
     *
     * @return the warnings file name
     */
    protected abstract String getWarningsFile();
}
