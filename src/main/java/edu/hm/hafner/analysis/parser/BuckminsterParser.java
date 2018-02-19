package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for Buckminster compiler warnings.
 *
 * @author Johannes Utzig
 */
public class BuckminsterParser extends RegexpLineParser {
    private static final long serialVersionUID = -3723799140297979579L;

    private static final String BUCKMINSTER_WARNING_PATTERN = "^.*(Warning|Error): file (.*?)(, line )?(\\d*): (.*)$";

    /**
     * Creates a new instance of {@link BuckminsterParser}.
     */
    public BuckminsterParser() {
        super(BUCKMINSTER_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        Priority priority = "Error".equalsIgnoreCase(matcher.group(1)) ? Priority.HIGH : Priority.NORMAL;
        return builder.setFileName(matcher.group(2)).setLineStart(parseInt(matcher.group(4)))
                      .setCategory(guessCategory(matcher.group(5))).setMessage(matcher.group(5))
                      .setPriority(priority).build();

    }
}

