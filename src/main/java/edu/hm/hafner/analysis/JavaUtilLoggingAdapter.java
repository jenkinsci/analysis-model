package edu.hm.hafner.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.hm.hafner.analysis.Report.IssuePrinter;

/**
 * Adapter fuer die Ausgabe von Issues auf einem Java Util Logger.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified 28/06/2020
 */
public class JavaUtilLoggingAdapter implements IssuePrinter {
    /**
     * Ein Java Util Logger, der adaptiert wird.
     */
    private final Logger logger;

    /**
     * Bildet die Strengen von Issues auf ein entsprechendes Level eines Java Util Loggers ab.
     */
    private final Map<Severity, Level> severityToLevel;

    /**
     * Konstruktor der Klasse JavaUtilLoggingAdapter.
     *
     * @param logger
     *         Ein Java Util Logger, welcher adaptiert wird.
     */
    public JavaUtilLoggingAdapter(final Logger logger) {
        this.logger = logger;
        severityToLevel = new HashMap<>();

        severityToLevel.put(Severity.ERROR, Level.SEVERE);
        severityToLevel.put(Severity.WARNING_HIGH, Level.WARNING);
        severityToLevel.put(Severity.WARNING_NORMAL, Level.INFO);
        severityToLevel.put(Severity.WARNING_LOW, Level.FINE);
    }

    @Override
    public void print(final Issue issue) {
        Objects.requireNonNull(issue);

        final Level logLevel = severityToLevel.get(issue.getSeverity());

        if (Objects.isNull(logLevel)) {
            throw new IllegalArgumentException("Issue contains an unsupported severity.");
        }

        logger.log(logLevel, issue.getMessage());
    }
}
