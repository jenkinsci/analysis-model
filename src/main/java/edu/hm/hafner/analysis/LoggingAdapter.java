package edu.hm.hafner.analysis;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * An Adapter to Log Issues via java.util.
 *
 * @author Tobias Karius
 */
public class LoggingAdapter implements IssuePrinter {

    /**
     * The java.util Logger for the Adapter.
     */
    private final Logger logger;

    /**
     * The Constructor to initialize the logger in the test.
     * @param logger The java.util Logger
     */
    @VisibleForTesting
    public LoggingAdapter(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(final Issue issue) {
        Severity severity = issue.getSeverity();
        String issueString = issue.toString();

        if (Severity.ERROR.equals(severity)) {
            logger.log(Level.SEVERE, issueString);
        }
        else if (Severity.WARNING_HIGH.equals(severity)) {
            logger.log(Level.WARNING, issueString);
        }
        else if (Severity.WARNING_NORMAL.equals(severity)) {
            logger.log(Level.INFO, issueString);
        }
        else {
            logger.log(Level.FINE, issueString);
        }
    }


}
