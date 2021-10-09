package edu.hm.hafner.analysis;

import java.util.Arrays;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.errorprone.annotations.FormatMethod;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Filters the log of a {@link Report} instance so that only a limited number of errors will be reported. If the number
 * of errors exceeds this limit, then subsequent error messages will be skipped.
 *
 * @author Ullrich Hafner
 */
public class FilteredLog {
    private static final int DEFAULT_MAX_LINES = 20;

    private final Report delegate;
    private final String title;
    private final int maxLines;
    private int lines = 0;

    /**
     * Creates a new {@link FilteredLog} for a {@link Report}. Number of printed errors: {@link #DEFAULT_MAX_LINES}.
     *
     * @param report
     *         the report to filter the error log
     * @param title
     *         the title of the error messages
     */
    public FilteredLog(final Report report, final String title) {
        this(report, title, DEFAULT_MAX_LINES);
    }

    /**
     * Creates a new {@link FilteredLog} for a {@link Report}.
     *
     * @param report
     *         the report to filter the error log
     * @param title
     *         the title of the error messages
     * @param maxLines
     *         the maximum number of lines to log
     */
    @SuppressFBWarnings(value = "EI", justification = "Report is used as a delegate")
    public FilteredLog(final Report report, final String title, final int maxLines) {
        delegate = report;
        this.title = title;
        this.maxLines = maxLines;
    }

    /**
     * Logs the specified information message. Use this method to log any useful information when composing this
     * log.
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
     * Logs the specified error message. Use this method to log any error when composing this log.
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
        printTitle();

        if (lines < maxLines) {
            delegate.logError(format, args);
        }
        lines++;
    }

    private void printTitle() {
        if (lines == 0) {
            delegate.logError("%s", title);
        }
    }

    /**
     * Logs the specified exception. Use this method to log any exception when composing this log.
     *
     * @param exception
     *         the exception to log
     * @param format
     *         A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args
     *         Arguments referenced by the format specifiers in the format string.  If there are more arguments than
     *         format specifiers, the extra arguments are ignored.  The number of arguments is variable and may be
     *         zero.
     */
    @FormatMethod
    public void logException(final Exception exception, final String format, final Object... args) {
        printTitle();

        if (lines < maxLines) {
            delegate.logError(format, args);
            Arrays.stream(ExceptionUtils.getRootCauseStackTrace(exception)).forEach(s -> delegate.logError("%s", s));
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
        if (lines > maxLines) {
            delegate.logError("  ... skipped logging of %d additional errors ...", lines - maxLines);
        }
    }
}
