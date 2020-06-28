package edu.hm.hafner.analysis;

import org.slf4j.Logger;
import org.slf4j.simple.SimpleLoggerFactory;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.util.VisibleForTesting;
public class SLF4JLogger implements IssuePrinter {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
    }
    private final Logger logger;

    public SLF4JLogger(final String className) {
        this(new SimpleLoggerFactory().getLogger(className));
    }

    @VisibleForTesting
    SLF4JLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(final Issue issue) {
        Severity severity = issue.getSeverity();
        final String logMessageOfIssue = issue.toString();
        // check um welche issue severity es sich handelt und mit dem entsprechendem JDK Level loggen
        if(severity.equals(Severity.WARNING_NORMAL)) {
            logger.info(logMessageOfIssue); // zum Testen mit mock() info() anstelle von atInfo()
        }
        else if(severity.equals(Severity.WARNING_HIGH)) {
            logger.atWarn().log(logMessageOfIssue);
        }
        else if(severity.equals(Severity.WARNING_LOW)) {
            logger.atTrace().log(logMessageOfIssue);
        }
        else if(severity.equals(Severity.ERROR)) {
            logger.atError().log(logMessageOfIssue);
        }
        else
            throw new IllegalArgumentException("Unknown Severity of Issue for SLFJLogger :" + issue.getSeverity().getName());
    }
}
