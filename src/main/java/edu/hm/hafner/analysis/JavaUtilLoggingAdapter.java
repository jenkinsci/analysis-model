package edu.hm.hafner.analysis;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.hm.hafner.analysis.Report.IssuePrinter;


/**
 * Adapter class for the JavaUtilLogger
 *
 * @author Matthias König
 */

public class JavaUtilLoggingAdapter implements IssuePrinter {
    private final Logger javaUtilLogger;

    JavaUtilLoggingAdapter(final Logger inputLogger) {
        this.javaUtilLogger = inputLogger;
    }

    @Override
    public void print(final Issue issue) {
        Severity issueSeverity = issue.getSeverity();

        if (Severity.WARNING_LOW.equals(issueSeverity)) {
            javaUtilLogger.log(Level.FINE, issue.toString());
        } else if (Severity.WARNING_NORMAL.equals(issueSeverity)) {
            javaUtilLogger.log(Level.INFO, issue.toString());
        } else if (Severity.WARNING_HIGH.equals(issueSeverity)) {
            javaUtilLogger.log(Level.WARNING, issue.toString());
        } else if (Severity.ERROR.equals(issueSeverity)) {
            javaUtilLogger.log(Level.SEVERE, issue.toString());
        }
    }
}