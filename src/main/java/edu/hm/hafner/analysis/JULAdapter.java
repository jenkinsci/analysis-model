package edu.hm.hafner.analysis;

import java.util.logging.*;
import edu.hm.hafner.analysis.Report.IssuePrinter;

/**
 * Using the Adapter Pattern to log issues to Java Util Logging.
 *
 * @author budelmann
 */
public class JULAdapter implements IssuePrinter {

    /** The Logger instance. */
    private final Logger logger;

    /**
     * Creates a new Printer that logs to Java Util Logging.
     *
     * @param logger
     *         the logger instance
     */
    public JULAdapter(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(final Issue issue) {
        logger.log(fromSeverityToLevel(issue.getSeverity()), issue.toString());
    }

    /**
     * Lookup Method for getting the JDK Level from the Issue severity.
     *
     * @param severity
     *         the issue severity
     *
     * @return the JDK Level
     */
    private Level fromSeverityToLevel(final Severity severity) {
        final Level level;

        if (severity.equals(Severity.ERROR)) {
            level = Level.SEVERE;
        }
        else if(severity.equals(Severity.WARNING_HIGH)) {
            level = Level.WARNING;
        }
        else if (severity.equals(Severity.WARNING_NORMAL)) {
            level = Level.INFO;
        }
        else {
            level = Level.FINE;
        }
        return level;
    }
}
