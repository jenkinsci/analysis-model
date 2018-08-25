package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for the IAR C/C++ compiler warnings. Note, that since release 4.1 this parser requires that IAR compilers
 * are started with option '----no_wrap_diagnostics'. Then the IAR compilers will create single-line warnings.
 *
 * @author Claus Klein
 * @author Ullrich Hafner
 */
public class IarParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 7695540852439013425L;

    private static final String IAR_WARNING_PATTERN = ANT_TASK 
            + "(?:(.*)\\((\\d+)\\) : )?(Error|Remark|Warning|Fatal [Ee]rror)\\[(\\w+)\\]: (.*)$";

    /**
     * Creates a new instance of {@link IarParser}.
     */
    public IarParser() {
        super(IAR_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning") || line.contains("rror") || line.contains("Remark");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setPriority(mapPriority(matcher))
                .setMessage(normalizeWhitespaceInMessage(matcher.group(5)))
                .setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(matcher.group(4))
                .build();
    }

    private Priority mapPriority(final Matcher matcher) {
        Priority priority;
        if ("Remark".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.LOW;
        }
        else if ("Error".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.HIGH;
        }
        else if ("Fatal error".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }
        return priority;
    }

    private String normalizeWhitespaceInMessage(final String message) {
        return message.replaceAll("\\s+", " ");
    }
}
