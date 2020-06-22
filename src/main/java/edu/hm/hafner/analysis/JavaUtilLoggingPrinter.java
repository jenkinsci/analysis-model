package edu.hm.hafner.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.util.VisibleForTesting;

public class JavaUtilLoggingPrinter implements IssuePrinter {
    private final Logger logger;
    private final Map<Severity, Level> map = new HashMap<Severity, Level>() {{
        put(Severity.ERROR, Level.SEVERE);
        put(Severity.WARNING_HIGH, Level.WARNING);
        put(Severity.WARNING_NORMAL, Level.INFO);
        put(Severity.WARNING_LOW, Level.FINE);
    }};

    public JavaUtilLoggingPrinter() {
        this.logger = Logger.getLogger(Report.class.getPackage().getName());
    }

    @VisibleForTesting
    JavaUtilLoggingPrinter(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void print(final Issue issue) {
        if(!map.containsKey(issue.getSeverity()))
            throw new IllegalArgumentException("Could not convert IssueSeverity to java.util.logging.Level");
        logger.log(map.get(issue.getSeverity()), issue.toString());
    }
}
