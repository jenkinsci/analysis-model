package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import static edu.hm.hafner.analysis.Categories.guessCategoryIfEmpty;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the Sphinx build warnings.
 *
 * @author Robert Williams
 */
public class SphinxBuildParser extends RegexpLineParser {
    private static final long serialVersionUID = 1483050615340274588L;

    private static final String SPHINX_BUILD_WARNING_PATTERN = "^(.*):(\\d+|None|): (.*?): (.*)";

    /**
     * Creates a new instance of {@link SphinxBuildParser}.
     */
    public SphinxBuildParser() {
        super(SPHINX_BUILD_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
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
        if ("error".equalsIgnoreCase(priority)) {
            return Severity.WARNING_HIGH;
        }
        else {
            return Severity.WARNING_NORMAL;
        }
    }
}

