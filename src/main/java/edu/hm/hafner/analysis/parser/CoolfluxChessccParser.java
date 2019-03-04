package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the Coolflux DSP Compiler warnings.
 *
 * @author Vangelis Livadiotis
 */
public class CoolfluxChessccParser extends RegexpLineParser {
    private static final long serialVersionUID = 4742509996511002391L;

    private static final String CHESSCC_PATTERN = "^.*?Warning in \"([^\"]+)\", line (\\d+),.*?:\\s*(.*)$";

    /**
     * Creates a new instance of {@link CoolfluxChessccParser}.
     */
    public CoolfluxChessccParser() {
        super(CHESSCC_PATTERN);
    }

    @Override
    protected String interestingLineContent(String line) {
        if (line.contains("Warning")) {
            return line;
        }

        return null;
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setMessage(matcher.group(3))
                .setSeverity(Severity.WARNING_HIGH)
                .buildOptional();
    }
}


