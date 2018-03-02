package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for PRQA QA-C Sourcecode Analyser warnings.
 *
 * @author Sven Lübke
 */
public class QacSourceCodeAnalyserParser extends RegexpLineParser {
    private static final long serialVersionUID = -8104046102312005968L;

    /** Pattern of QA-C Sourcecode Analyser warnings. */
    private static final String QAC_WARNING_PATTERN = "^(.+?)\\((\\d+),(\\d+)\\): (Err|Msg)\\((\\d+):(\\d+)\\) (.+?)$";

    /**
     * Creates a new instance of {@code QACSourceCodeAnalyserParser}.
     */
    public QacSourceCodeAnalyserParser() {
        super(QAC_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
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

        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(category)
                .setMessage(matcher.group(7))
                .setPriority(priority)
                .build();
    }
}

