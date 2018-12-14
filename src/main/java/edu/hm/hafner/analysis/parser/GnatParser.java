package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the Gnat compiler warnings.
 *
 * @author Bernhard Berger
 */
public class GnatParser extends RegexpLineParser {
    private static final long serialVersionUID = -7139298560308123856L;

    private static final String GNAT_WARNING_PATTERN = "^(.+.(?:ads|adb)):(\\d+):(\\d+): ((?:error:)|(?:warning:)|"
            + "(?:\\(style\\))) (.+)$";

    /**
     * Creates a new instance of {@link GnatParser}.
     */
    public GnatParser() {
        super(GNAT_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        Severity priority;
        String category;

        if ("warning:".equalsIgnoreCase(matcher.group(4))) {
            priority = Severity.WARNING_NORMAL;
            category = "GNAT warning";
        }
        else if ("(style)".equalsIgnoreCase(matcher.group(4))) {
            priority = Severity.WARNING_LOW;
            category = "GNAT style";
        }
        else {
            priority = Severity.WARNING_HIGH;
            category = "GNAT error";
        }
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(category)
                .setMessage(matcher.group(5))
                .setSeverity(priority)
                .buildOptional();
    }
}
