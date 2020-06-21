package edu.hm.hafner.analysis;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.util.VisibleForTesting;

/**
 * Ein Adapter fur den Java Util Logger.
 * @author Michael Schober
 */
public class JavaUtilLoggingAdapter implements IssuePrinter {

    /** ein java util logger */
    private static Logger logr;

    @VisibleForTesting
    JavaUtilLoggingAdapter( Logger logger){
        logr = logger;
    }

    @Override
    public void print(final Issue issue) {
        Severity severity = issue.getSeverity();
        if (Severity.ERROR.equals(severity)) {
            logr.log(Level.SEVERE, issue.toString());
        }
        else if (Severity.WARNING_HIGH.equals(severity)) {
            logr.log(Level.WARNING, issue.toString());
        }
        else if (Severity.WARNING_NORMAL.equals(severity)) {
            logr.log(Level.INFO, issue.toString());
        }
        else {
            logr.log(Level.FINE, issue.toString());
        }
    }

}
