package edu.hm.hafner.analysis;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.hm.hafner.analysis.Report.IssuePrinter;

/**
 * Issue printer that prints the message to a Java Util Logger
 * 
 * @author Johannes Jäger
 */
public class JavaUtilLoggingPrinter implements IssuePrinter {

    private final Logger logger;

    JavaUtilLoggingPrinter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(Issue issue) {
        final Severity severity = issue.getSeverity();
        if (severity.equals(Severity.ERROR)) {
            logger.log(Level.SEVERE, issue.toString());
        } else if (severity.equals(Severity.WARNING_HIGH)) {
            logger.log(Level.WARNING, issue.toString());
        } else if (severity.equals(Severity.WARNING_NORMAL)) {
            logger.log(Level.INFO, issue.toString());
        } else if (severity.equals(Severity.WARNING_LOW)) {
            logger.log(Level.FINE, issue.toString());
        }
    }
}
