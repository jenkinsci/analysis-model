package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.analysis.Categories.*;

/**
 * A parser for the golint tool in the Go toolchain.
 *
 * @author Ryan Cox
 */
public class GoLintParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -5895416507693444713L;

    // conn.go:360:3: should replace c.writeSeq += 1 with c.writeSeq++
    private static final String GOLINT_WARNING_PATTERN = "^(.*?):(\\d+?):(\\d*?):\\s*(.*)$";

    /**
     * Creates a new instance of {@link GoLintParser}.
     */
    public GoLintParser() {
        super(GOLINT_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        var message = matcher.group(4);
        var category = guessCategory(message);

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setColumnStart(matcher.group(3))
                .setCategory(category)
                .setMessage(message)
                .buildOptional();
    }
}
