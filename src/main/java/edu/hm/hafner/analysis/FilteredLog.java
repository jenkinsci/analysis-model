package edu.hm.hafner.analysis;

import com.google.errorprone.annotations.FormatMethod;

/**
 * Filters the log of a {@link Report} instance so that only a limited number of errors will be reported. If the number
 * of errors exceeds this limit, then subsequent error messages will be skipped.
 *
 * @author Ullrich Hafner
 */
public class FilteredLog {
    private static final String SKIPPED_MESSAGE = "  ... skipped logging of %d additional errors ...";

    private final Report delegate;
    private final String title;
    private int lines = 0;

    /**
     * Creates a new {@link FilteredLog} for a {@link Report}.
     *
     * @param report
     *         the report to filter the error log
     * @param title
     *         the title of the error messages
     */
    public FilteredLog(final Report report, final String title) {
        delegate = report;
        this.title = title;
    }

    /**
     * Logs the specified information message. Use this method to log any useful information when composing this
     * report.
     *
     * @param format
     *         A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args
     *         Arguments referenced by the format specifiers in the format string.  If there are more arguments than
     *         format specifiers, the extra arguments are ignored.  The number of arguments is variable and may be
     *         zero.
     */
    @FormatMethod
    public void logInfo(final String format, final Object... args) {
        delegate.logInfo(format, args);
    }

    /**
     * Logs the specified error message. Use this method to log any error when composing this report.
     *
     * @param format
     *         A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args
     *         Arguments referenced by the format specifiers in the format string.  If there are more arguments than
     *         format specifiers, the extra arguments are ignored.  The number of arguments is variable and may be
     *         zero.
     */
    @FormatMethod
    public void logError(final String format, final Object... args) {
        if (lines == 0) {
            delegate.logError("%s", title);
        }
        if (lines < 5) {
            delegate.logError(format, args);
        }
        lines++;
    }

    /**
     * Returns the total number of errors that have been reported.
     *
     * @return the total number of errors
     */
    public int size() {
        return lines;
    }

    /**
     * Writes a summary message to the reports' error log that denotes the total number of errors that have been
     * reported.
     */
    public void logSummary() {
        if (lines > 5) {
            delegate.logError(SKIPPED_MESSAGE, lines - 5);
        }
    }
}
