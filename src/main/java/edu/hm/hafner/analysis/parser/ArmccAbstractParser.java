package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Base class for Armcc 5 parsers.
 *
 * @author Ullrich Hafner
 */
class ArmccAbstractParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 7695526846703329161L;

    ArmccAbstractParser(final String pattern) {
        super(pattern);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"))
                .setMessage(matcher.group("code") + " - " + matcher.group("message"))
                .setSeverity(Severity.guessFromString(matcher.group("severity")))
                .buildOptional();
    }
}
