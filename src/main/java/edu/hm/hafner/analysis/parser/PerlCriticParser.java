package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.analysis.util.IntegerParser.*;

/**
 * A parser for the Perl::Critic warnings.
 *
 * @author Mihail Menev, menev@hm.edu
 */
public class PerlCriticParser extends LookaheadParser {
    private static final long serialVersionUID = -6481203155449490873L;

    private static final String PERLCRITIC_WARNING_PATTERN = "(?:(.*?):)?(.*)\\s+at\\s+line\\s+(\\d+),\\s+column\\s+"
            + "(\\d+)\\.\\s*(?:See page[s]?\\s+)?(.*)\\.\\s*\\(?Severity:\\s*(\\d)\\)?";
    private static final int SEVERITY_LOW_LIMIT = 2;
    private static final int SEVERITY_NORMAL_LIMIT = 4;

    /**
     * Creates a new instance of {@link PerlCriticParser}.
     */
    public PerlCriticParser() {
        super(PERLCRITIC_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String filename;
        if (matcher.group(1) == null) {
            filename = "-";
        }
        else {
            filename = matcher.group(1);
        }

        return builder.setFileName(filename)
                .setLineStart(matcher.group(3))
                .setColumnStart(matcher.group(4))
                .setCategory(matcher.group(5))
                .setMessage(matcher.group(2))
                .setSeverity(checkPriority(parseInt(matcher.group(6))))
                .buildOptional();
    }

    /**
     * Checks the severity level, parsed from the warning and return the priority level.
     *
     * @param priority
     *         the severity level of the warning.
     *
     * @return the priority level.
     */
    private Severity checkPriority(final int priority) {
        if (priority < SEVERITY_LOW_LIMIT) {
            return Severity.WARNING_LOW;
        }
        else if (priority < SEVERITY_NORMAL_LIMIT) {
            return Severity.WARNING_NORMAL;
        }
        else {
            return Severity.WARNING_HIGH;
        }
    }
}
