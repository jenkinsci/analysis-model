package edu.hm.hafner.analysis;

import java.util.Objects;

import org.slf4j.Logger;

import edu.hm.hafner.analysis.Report.IssuePrinter;

/**
 * Adapter fuer die Ausgabe von Issues auf einem Logger der Simple Logging Facade fuer Java.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 28/06/2020
 */
public class SimpleLoggingFacadeAdapter implements IssuePrinter {
    /**
     * Ein Logger der Simple Logging Facade fuer Java, welcher adaptiert wird.
     */
    private final Logger logger;

    /**
     * Konstruktor der Klasse SimpleLoggingFacadeAdapter.
     *
     * @param logger
     *         Ein Logger der Simple Logging Facade fuer Java, der adaptiert wird.
     */
    public SimpleLoggingFacadeAdapter(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(final Issue issue) {
        Objects.requireNonNull(issue);

        final String issueMessage = issue.getMessage();
        final Severity issueSeverity = issue.getSeverity();

        if (Severity.ERROR.equals(issueSeverity)) {
            logger.error(issueMessage);
        }
        else if (Severity.WARNING_HIGH.equals(issueSeverity)) {
            logger.warn(issueMessage);
        }
        else if (Severity.WARNING_NORMAL.equals(issueSeverity)) {
            logger.info(issueMessage);
        }
        else if (Severity.WARNING_LOW.equals(issueSeverity)) {
            logger.trace(issueMessage);
        }
        else {
            throw new IllegalArgumentException("Issue contains an unsupported severity.");
        }
    }
}
