package edu.hm.hafner.analysis;

import org.slf4j.Logger;

import edu.hm.hafner.analysis.Report.IssuePrinter;

/**
 * Issue printer that prints the message to a SLF4J Logger
 * 
 * @author Johannes Jäger
 */
public class SLF4JLoggingPrinter implements IssuePrinter {

    private final Logger logger;

    SLF4JLoggingPrinter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(Issue issue) {
        final Severity severity = issue.getSeverity();
        if (severity.equals(Severity.ERROR)) {
            logger.error(issue.toString());
        } else if (severity.equals(Severity.WARNING_HIGH)) {
            logger.warn(issue.toString());
        } else if (severity.equals(Severity.WARNING_NORMAL)) {
            logger.info(issue.toString());
        } else if (severity.equals(Severity.WARNING_LOW)) {
            logger.trace(issue.toString());
        }
    }
}
