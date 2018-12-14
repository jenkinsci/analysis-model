package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import static edu.hm.hafner.analysis.Categories.guessCategory;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
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
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        Severity priority = "Error".equalsIgnoreCase(matcher.group(1)) ? Severity.WARNING_HIGH : Severity.WARNING_NORMAL;
        return builder.setFileName(matcher.group(2)).setLineStart(matcher.group(4))
                      .setCategory(guessCategory(matcher.group(5))).setMessage(matcher.group(5))
                      .setSeverity(priority).buildOptional();

    }
}

