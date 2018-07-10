package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the ruboCop warnings.
 *
 * @author David van Laatum
 */
public class RuboCopParser extends RegexpLineParser {
    private static final long serialVersionUID = 7199325311690082783L;

    private static final String RUBOCOP_WARNING_PATTERN = "^([^:]+):(\\d+):(\\d+): ([RCWEF]): (\\S+): (.*)$";

    /**
     * Creates a new instance of {@link RuboCopParser}.
     */
    public RuboCopParser() {
        super(RUBOCOP_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(6);
        String category = guessCategoryIfEmpty(matcher.group(5), message);

        String severity = matcher.group(4);
        Priority priority = Priority.NORMAL;
        if ("E".equals(severity) || "F".equals(severity)) {
            priority = Priority.HIGH;
        }

        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(category)
                .setMessage(message)
                .setPriority(priority)
                .setColumnStart(parseInt(matcher.group(3)))
                .build();
    }
}

