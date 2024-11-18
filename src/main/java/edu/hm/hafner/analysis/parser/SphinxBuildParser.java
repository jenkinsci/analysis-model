package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.analysis.Categories.*;

/**
 * A parser for the Sphinx build warnings.
 *
 * @author Robert Williams
 */
public class SphinxBuildParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 1483050615340274588L;

    private static final String SPHINX_BUILD_WARNING_PATTERN = "^([a-zA-Z]:\\\\.*?|/.*?|[^\\/].*?):(?:|(?:.* of .*:)?(\\d+|None|):) (ERROR|WARNING): (.*)";

    /**
     * Creates a new instance of {@link SphinxBuildParser}.
     */
    public SphinxBuildParser() {
        super(SPHINX_BUILD_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String message = matcher.group(4);
        String category = guessCategoryIfEmpty(matcher.group(3), message);

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(category)
                .setMessage(message)
                .setSeverity(mapPriority(category))
                .buildOptional();
    }

    private Severity mapPriority(final String priority) {
        if (equalsIgnoreCase(priority, "error")) {
            return Severity.WARNING_HIGH;
        }
        else {
            return Severity.WARNING_NORMAL;
        }
    }
}
