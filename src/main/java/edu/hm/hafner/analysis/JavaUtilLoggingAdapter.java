package edu.hm.hafner.analysis;

import java.util.logging.Logger;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.util.VisibleForTesting;

import java.util.logging.Level;

/**
 * Adapter to print Issues via Java Util Logging.
 */
public class JavaUtilLoggingAdapter implements IssuePrinter {
    /**
     * The Logger of JavaUtil.
     */

    private final Logger logger;

    /**
     * JavaUtilLoggingAdapter Constructor.
     *
     * @param logger
     *         a logger.
     */
    @VisibleForTesting
    public JavaUtilLoggingAdapter(final Logger logger) {
        this.logger = logger;

    }

    @Override
    public void print(final Issue issue) {
        final Severity severity = issue.getSeverity();
        final String issueString = issue.toString();
        if (severity.equals(Severity.ERROR)) {
            logger.log(Level.SEVERE, issueString);
        }
        else if (severity.equals(Severity.WARNING_HIGH)) {
            logger.log(Level.WARNING, issueString);
        }
        else if (severity.equals(Severity.WARNING_NORMAL)) {
            logger.log(Level.INFO, issueString);
        }
        else if (severity.equals(Severity.WARNING_LOW)) {
            logger.log(Level.FINE, issueString);
        }
    }
}
