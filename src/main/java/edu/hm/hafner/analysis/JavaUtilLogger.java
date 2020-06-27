package edu.hm.hafner.analysis;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.hm.hafner.analysis.Report.IssuePrinter;

public class JavaUtilLogger implements IssuePrinter {
    private final Logger logger;

    public JavaUtilLogger(String filename) {
        this.logger = Logger.getLogger(filename);
    }

    // todo: sieht auch etwas nach template method pattern aus => könnte man IssueReport zur abstrakten Klasse AbstractIssueReport machen. Die template-method wäre print(issue) und als abstract-methods logNormal, logHigh, ...
    @Override
    public void print(final Issue issue) {
        Severity severity = issue.getSeverity();
        final String logMessageOfIssue = issue.toString();

        // check um weclche issue severity es sich handelt und mit dem entsprechendem JDK Level loggen
        if(severity.equals(Severity.WARNING_NORMAL))
            logger.log(Level.INFO, logMessageOfIssue);
        else if(severity.equals(Severity.WARNING_HIGH))
            logger.log(Level.WARNING, logMessageOfIssue);
        else if(severity.equals(Severity.WARNING_LOW))
            logger.log(Level.FINE, logMessageOfIssue);
        else if(severity.equals(Severity.ERROR))
            logger.log(Level.SEVERE, logMessageOfIssue);
        else
            throw new IllegalArgumentException("Unknown Severity of Issue for JavaUtilLogger: " + issue.getSeverity().getName());
    }
}
