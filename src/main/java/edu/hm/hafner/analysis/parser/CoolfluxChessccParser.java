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
 * A parser for the Coolflux DSP Compiler warnings.
 *
 * @author Vangelis Livadiotis
 */
public class CoolfluxChessccParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 4742509996511002391L;

    private static final String CHESSCC_PATTERN = "^.*?Warning in \"([^\"]+)\", line (\\d+),.*?:\\s*(.*)$";

    /**
     * Creates a new instance of {@link CoolfluxChessccParser}.
     */
    public CoolfluxChessccParser() {
        super(CHESSCC_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setMessage(matcher.group(3))
                .setSeverity(Severity.WARNING_HIGH)
                .buildOptional();
    }
}
