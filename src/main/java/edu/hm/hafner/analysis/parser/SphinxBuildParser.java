package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
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
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(4);
        String category = guessCategoryIfEmpty(matcher.group(3), message);

        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(category)
                .setMessage(message)
                .setPriority(mapPriority(category))
                .build();
    }

    private Priority mapPriority(final String priority) {
        if ("error".equalsIgnoreCase(priority)) {
            return Priority.HIGH;
        }
        else {
            return Priority.NORMAL;
        }
    }
}

