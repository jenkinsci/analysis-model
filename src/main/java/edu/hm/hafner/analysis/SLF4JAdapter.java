package edu.hm.hafner.analysis;

import edu.hm.hafner.analysis.Report.IssuePrinter;

import org.slf4j.Logger;

/**
 * Adapter class for the SLF4JLogger
 *
 * @author Matthias König
 */

public class SLF4JAdapter implements IssuePrinter {
    private final Logger slf4jLogger;

    SLF4JAdapter(final Logger inputLogger) {
        this.slf4jLogger = inputLogger;
    }

    @Override
    public void print(final Issue issue) {
        Severity issueSeverity = issue.getSeverity();

        if (Severity.WARNING_LOW.equals(issueSeverity)) {
            slf4jLogger.trace(issue.toString());
        } else if (Severity.WARNING_NORMAL.equals(issueSeverity)) {
            slf4jLogger.info(issue.toString());
        } else if (Severity.WARNING_HIGH.equals(issueSeverity)) {
            slf4jLogger.warn(issue.toString());
        } else if (Severity.ERROR.equals(issueSeverity)) {
            slf4jLogger.error(issue.toString());
        }
    }
}