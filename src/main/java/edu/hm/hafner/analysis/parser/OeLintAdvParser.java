package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * A parser for oelint-adv.
 */
public class OeLintAdvParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -64727714887068026L;

    /**
     * Creates a new instance of {@link OeLintAdvParser}.
     */
    public OeLintAdvParser() {
        super("^(?<file>.+?):(?<line>[0-9]+?):(?<severity>.+?):(?<category>.+?):(?<message>.+?)$");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) throws ParsingException {
        return builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"))
                .setCategory(matcher.group("category"))
                .setMessage(matcher.group("message"))
                .setSeverity(Severity.guessFromString(matcher.group("severity")))
                .buildOptional();
    }
}
