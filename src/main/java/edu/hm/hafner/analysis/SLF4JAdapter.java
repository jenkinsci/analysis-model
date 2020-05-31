package edu.hm.hafner.analysis;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * Ein Adapter fuer den SLF4J Logger.
 * @author Michael Schober
 */
public class SLF4JAdapter implements IssuePrinter {

    /** ein slf4j logger */
    private static org.slf4j.Logger logger;

    @VisibleForTesting
    SLF4JAdapter(final org.slf4j.Logger logr){
        this.logger = logr;
    }

    @Override
    public void print(final Issue issue) {
        Severity severity = issue.getSeverity();
        if (Severity.ERROR.equals(severity)) {
            logger.error(issue.toString());
        }
        else if (Severity.WARNING_HIGH.equals(severity)) {
            logger.warn(issue.toString());
        }
        else if (Severity.WARNING_NORMAL.equals(severity)) {
            logger.info(issue.toString());
        }
        else {
            logger.trace(issue.toString());
        }
    }
}



