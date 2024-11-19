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
 * A parser for the scalac compiler warnings. You should use -feature and -deprecation compiler opts.
 *
 * @author Alexey Kislin
 */
public class ScalacParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -4034552404001800574L;

    private static final String SCALAC_WARNING_PATTERN = "^(\\[WARNING\\]|\\[ERROR\\])\\s*(.*):(\\d+):\\s*([a-z]*)"
            + ":\\s*(.*)$";

    /**
     * Creates a new instance of {@link ScalacParser}.
     */
    public ScalacParser() {
        super(SCALAC_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setFileName(matcher.group(2))
                .setLineStart(matcher.group(3))
                .setCategory(matcher.group(4))
                .setMessage(matcher.group(5))
                .setSeverity(mapPriority(matcher))
                .buildOptional();
    }

    private Severity mapPriority(final Matcher matcher) {
        return "[ERROR]".equals(matcher.group(1)) ? Severity.WARNING_HIGH : Severity.WARNING_NORMAL;
    }
}
