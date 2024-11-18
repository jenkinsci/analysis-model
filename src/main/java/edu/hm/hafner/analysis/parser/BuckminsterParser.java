package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.analysis.Categories.*;

/**
 * A parser for Buckminster compiler warnings.
 *
 * @author Johannes Utzig
 */
public class BuckminsterParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -3723799140297979579L;

    private static final String BUCKMINSTER_WARNING_PATTERN = "^.*(Warning|Error): file (.*?)(, line )?(\\d*): (.*)$";

    /**
     * Creates a new instance of {@link BuckminsterParser}.
     */
    public BuckminsterParser() {
        super(BUCKMINSTER_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        Severity priority = equalsIgnoreCase(matcher.group(1), "Error") ? Severity.WARNING_HIGH : Severity.WARNING_NORMAL;
        return builder.setFileName(matcher.group(2)).setLineStart(matcher.group(4))
                      .setCategory(guessCategory(matcher.group(5))).setMessage(matcher.group(5))
                      .setSeverity(priority).buildOptional();
    }
}
