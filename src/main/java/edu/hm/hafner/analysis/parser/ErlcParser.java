package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the erlc compiler warnings.
 *
 * @author Stefan Brausch
 */
public class ErlcParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 8986478184830773892L;

    private static final String ERLC_WARNING_PATTERN = "^(.+\\.(?:erl|yrl|mib|bin|rel|asn1|idl)):(\\d*): ([wW]arning:"
            + " )?(.+)$";

    /**
     * Creates a new instance of {@link ErlcParser}.
     */
    public ErlcParser() {
        super(ERLC_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        Severity priority;
        String category;
        var categoryMatch = matcher.group(3);

        if (equalsIgnoreCase(categoryMatch, "warning: ")) {
            priority = Severity.WARNING_NORMAL;
            category = categoryMatch.substring(0, categoryMatch.length() - 2);
        }
        else {
            priority = Severity.WARNING_HIGH;
            category = "Error";
        }
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(category)
                .setMessage(matcher.group(4))
                .setSeverity(priority)
                .buildOptional();
    }
}
