package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for Metrowerks Codewarrior 4.x compiler warnings.
 *
 * @author Sven Lübke
 */
public class MetrowerksCwCompilerParser extends RegexpLineParser {
    private static final long serialVersionUID = 4317595592384426180L;

    /** Pattern of MW CodeWarrior compiler warnings. */
    private static final String CW_COMPILER_WARNING_PATTERN = "^(.+?)\\((\\d+)\\): (INFORMATION|WARNING|ERROR) (.+?):"
            + " (.*)$";

    /**
     * Creates a new instance of {@link MetrowerksCwCompilerParser}.
     */
    public MetrowerksCwCompilerParser() {
        super(CW_COMPILER_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String fileName = matcher.group(1);
        int lineNumber = parseInt(matcher.group(2));
        String message = matcher.group(5);
        Priority priority;

        String category;
        if ("error".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.HIGH;
            category = "ERROR";
        }
        else if ("information".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.LOW;
            category = "Info";
        }
        else {
            priority = Priority.NORMAL;
            category = "Warning";
        }
        return builder.setFileName(fileName).setLineStart(lineNumber).setCategory(category).setMessage(message)
                      .setPriority(priority).build();
    }
}

