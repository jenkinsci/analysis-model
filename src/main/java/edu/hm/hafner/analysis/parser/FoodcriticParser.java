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
 * A parser for Foodcritic warnings.
 *
 * @author Rich Schumacher
 */
public class FoodcriticParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -5338881031392241140L;

    private static final String FOODCRITIC_WARNING_PATTERN =
            "^(?<category>FC\\d+): (?<message>[^:]+): (?<file>[^:]+):(?<line>\\d+)$";

    /**
     * Creates a new instance of {@link FoodcriticParser}.
     */
    public FoodcriticParser() {
        super(FOODCRITIC_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"))
                .setCategory(matcher.group("category"))
                .setMessage(matcher.group("message"))
                .setSeverity(Severity.WARNING_NORMAL)
                .buildOptional();
    }
}
