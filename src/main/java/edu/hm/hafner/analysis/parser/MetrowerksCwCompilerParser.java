package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

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
        Severity priority;
        String category;

        if ("error".equalsIgnoreCase(matcher.group(3))) {
            priority = Severity.WARNING_HIGH;
            category = "ERROR";
        }
        else if ("information".equalsIgnoreCase(matcher.group(3))) {
            priority = Severity.WARNING_LOW;
            category = "Info";
        }
        else {
            priority = Severity.WARNING_NORMAL;
            category = "Warning";
        }
        
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(category)
                .setMessage(matcher.group(5))
                .setSeverity(priority)
                .build();
    }
}

