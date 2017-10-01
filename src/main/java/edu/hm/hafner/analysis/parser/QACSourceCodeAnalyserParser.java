package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for PRQA QA-C Sourcecode Analyser warnings.
 *
 * @author Sven Lübke
 */
public class QACSourceCodeAnalyserParser extends RegexpLineParser {
    /** Pattern of QA-C Sourcecode Analyser warnings. */
    private static final String QAC_WARNING_PATTERN = "^(.+?)\\((\\d+),(\\d+)\\): (Err|Msg)\\((\\d+):(\\d+)\\) (.+?)$";

    /**
     * Creates a new instance of <code>QACSourceCodeAnalyserParser</code>.
     */
    public QACSourceCodeAnalyserParser() {
        super("qac", QAC_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        String fileName = matcher.group(1);
        int lineNumber = parseInt(matcher.group(2));
        String message = matcher.group(7);
        Priority priority;

        String category;
        if ("err".equalsIgnoreCase(matcher.group(4))) {
            priority = Priority.HIGH;
            category = "ERROR";
        }
        else {
            priority = Priority.NORMAL;
            category = "Warning";
        }
        return issueBuilder().setFileName(fileName).setLineStart(lineNumber).setCategory(category).setMessage(message)
                             .setPriority(priority).build();
    }
}

