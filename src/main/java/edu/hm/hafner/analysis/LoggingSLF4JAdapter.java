package edu.hm.hafner.analysis;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.util.VisibleForTesting;

import org.slf4j.Logger;

/**
 * An Adapter to log Issues via slf4j.
 *
 * @author Tobias Karius
 */
public class LoggingSLF4JAdapter implements IssuePrinter {

    /**
     * The slf4j Logger for the Adapter.
     */
    private final Logger logger;

    /**
     * The Constructor to initialize the logger in the test.
     * @param logger the slf4j Logger
     */
    @VisibleForTesting
    public LoggingSLF4JAdapter(final Logger logger) {
        this.logger = logger;
    }
    @Override
    public void print(final Issue issue) {
        Severity severity = issue.getSeverity();
        String issueString = issue.toString();

        if (Severity.ERROR.equals(severity)) {
            logger.error(issueString);
        }
        else if (Severity.WARNING_HIGH.equals(severity)) {
            logger.warn(issueString);
        }
        else if (Severity.WARNING_NORMAL.equals(severity)) {
            logger.info(issueString);
        }
        else {
            logger.trace(issueString);
        }
    }

}
