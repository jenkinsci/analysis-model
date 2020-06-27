package edu.hm.hafner.analysis;

import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.hm.hafner.analysis.Report.IssuePrinter;

public class SLF4JLogger implements IssuePrinter {
    private final Logger logger;

    public SLF4JLogger(final String className) {
        this.logger = LoggerFactory.getLogger(className);
    }

    @Override
    public void print(final Issue issue) {
        Severity severity = issue.getSeverity();
        final String logMessageOfIssue = issue.toString();

        // check um welche issue severity es sich handelt und mit dem entsprechendem JDK Level loggen
        if(severity.equals(Severity.WARNING_NORMAL))
            logger.atInfo().log(logMessageOfIssue);
        else if(severity.equals(Severity.WARNING_HIGH))
            logger.atWarn().log(logMessageOfIssue);
        else if(severity.equals(Severity.WARNING_LOW))
            logger.atTrace().log(logMessageOfIssue);
        else if(severity.equals(Severity.ERROR))
            logger.atError().log(logMessageOfIssue);
        else
            throw new IllegalArgumentException("Unknown Severity of Issue for SLFJLogger :" + issue.getSeverity().getName());
    }
}
