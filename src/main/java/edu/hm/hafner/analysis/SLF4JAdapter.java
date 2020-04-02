package edu.hm.hafner.analysis;

import org.slf4j.Logger;
import edu.hm.hafner.analysis.Report.IssuePrinter;

/**
 * Using the Adapter Pattern to log issues to Simple Logging Facade for Java(SLF4J).
 *
 * @author budelmann
 */
public class SLF4JAdapter implements IssuePrinter {

    /** The logger instance. */
    private final Logger logger;

    /**
     * Creates a new Printer that logs to Simple Logging Facade for Java(SLF4J).
     *
     * @param logger
     *         the logger instance
     */
    public SLF4JAdapter(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(final Issue issue) {
        if (issue.getSeverity().equals(Severity.ERROR)) {
            logger.error(issue.toString());
        }
        else if (issue.getSeverity().equals(Severity.WARNING_HIGH)) {
            logger.warn(issue.toString());
        }
        else if (issue.getSeverity().equals(Severity.WARNING_NORMAL)) {
            logger.info(issue.toString());
        }
        else {
            logger.trace(issue.toString());
        }
    }
}
