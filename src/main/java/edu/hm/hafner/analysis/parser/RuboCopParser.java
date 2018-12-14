package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import static edu.hm.hafner.analysis.Categories.guessCategoryIfEmpty;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the ruboCop warnings.
 *
 * @author David van Laatum
 */
public class RuboCopParser extends RegexpLineParser {
    private static final long serialVersionUID = 7199325311690082783L;

    private static final String RUBOCOP_WARNING_PATTERN = "^([^:]+):(\\d+):(\\d+): ([RCWEF]): (\\S+): (.*)$";
    private static final String ERROR = "E";
    private static final String FATAL = "F";

    /**
     * Creates a new instance of {@link RuboCopParser}.
     */
    public RuboCopParser() {
        super(RUBOCOP_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(6);
        String category = guessCategoryIfEmpty(matcher.group(5), message);

        String severity = matcher.group(4);
        Severity priority = Severity.WARNING_NORMAL;
        if (ERROR.equals(severity) || FATAL.equals(severity)) {
            priority = Severity.WARNING_HIGH;
        }

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(category)
                .setMessage(message)
                .setSeverity(priority)
                .setColumnStart(matcher.group(3))
                .buildOptional();
    }
}

