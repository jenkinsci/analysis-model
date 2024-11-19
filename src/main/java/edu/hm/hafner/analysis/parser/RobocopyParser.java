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
 * A parser for Robocopy.
 *
 * @author Adrian Deccico
 */
public class RobocopyParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -671744745118772873L;

    private static final String ROBOCOPY_WARNING_PATTERN = "^(.*)(EXTRA File|New File|same)\\s*(\\d*)\\s*(.*)$";

    /**
     * Creates a new instance of {@link RobocopyParser}.
     */
    public RobocopyParser() {
        super(ROBOCOPY_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        var file = matcher.group(4).split("\\s{11}", -1)[0];
        return builder.setFileName(file)
                .setLineStart(0)
                .setCategory(matcher.group(2))
                .setMessage(file)
                .setSeverity(Severity.WARNING_NORMAL)
                .buildOptional();
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("        ");
    }
}
