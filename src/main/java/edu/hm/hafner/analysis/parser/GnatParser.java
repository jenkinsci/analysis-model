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
 * A parser for the Gnat compiler warnings.
 *
 * @author Bernhard Berger
 */
public class GnatParser extends LookaheadParser {
    @Serial
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
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        Severity priority;
        String category;

        if (equalsIgnoreCase(matcher.group(4), "warning:")) {
            priority = Severity.WARNING_NORMAL;
            category = "GNAT warning";
        }
        else if (equalsIgnoreCase(matcher.group(4), "(style)")) {
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
