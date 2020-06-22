package edu.hm.hafner.analysis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.util.VisibleForTesting;

public class Slf4jPrinter implements IssuePrinter {

    private Logger logger;

    public Slf4jPrinter() {
        this.logger = LoggerFactory.getLogger(Report.class);
    }

    @VisibleForTesting
    public Slf4jPrinter(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(final Issue issue) {
        final String msg = issue.toString();
        Severity severity = issue.getSeverity();
        if (Severity.ERROR.equals(severity)) {
            logger.error(msg);
        }
        else if(Severity.WARNING_HIGH.equals(severity)){
            logger.warn(msg);
        }
        else if(Severity.WARNING_NORMAL.equals(severity)){
            logger.info(msg);
        }
        else if(Severity.WARNING_LOW.equals(severity)){
            logger.trace(msg);
        }
        else {
            throw new IllegalArgumentException("Could not convert IssueSeverity to slf4j.Logger");
        }
    }
}
