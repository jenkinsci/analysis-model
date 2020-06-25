package edu.hm.hafner.analysis;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * Adapter to print Issues via Simple Logging Facade for Java.
 */
public class SimpleLoggingFacadeAdapter implements IssuePrinter {
    /**
     * The Logger of JavaUtil.
     */

    private final org.slf4j.Logger logger;

    /**
     * Simple Logging Facade for Java Adapter Constructor.
     *
     * @param logger
     *         a logger.
     */
    @VisibleForTesting
    public SimpleLoggingFacadeAdapter(final org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(final Issue issue) {
        final Severity severity = issue.getSeverity();
        final String issueString = issue.toString();
        if(severity.equals(Severity.ERROR))
            logger.error(issueString);
        else if (severity.equals(Severity.WARNING_HIGH)) {
            logger.warn(issueString);
        }
        else if (severity.equals(Severity.WARNING_NORMAL)) {
            logger.info(issueString);
        }
        else if (severity.equals(Severity.WARNING_LOW)) {
            logger.trace(issueString);
        }
    }
}
