package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.util.IntegerParser.*;

/**
 * A parser for Cpplint static code checker warnings.
 *
 * @author Ullrich Hafner
 */
public class CppLintParser extends LookaheadParser {
    private static final long serialVersionUID = 1737791073711198075L;

    private static final String PATTERN = "^\\s*(.*)\\s*[(:](\\d+)\\)?:\\s*(.*)\\s*\\[(.*)\\] \\[(.*)\\]$";
    private static final int SEVERITY_HIGH_LIMIT = 5;
    private static final int SEVERITY_NORMAL_LIMIT = 3;

    /**
     * Creates a new instance of {@link CppLintParser}.
     */
    public CppLintParser() {
        super(PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        Severity priority = mapPriority(matcher.group(5));

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(4))
                .setMessage(matcher.group(3))
                .setSeverity(priority)
                .buildOptional();
    }

    private Severity mapPriority(final String priority) {
        int value = parseInt(priority);
        if (value >= SEVERITY_HIGH_LIMIT) {
            return Severity.WARNING_HIGH;
        }
        if (value >= SEVERITY_NORMAL_LIMIT) {
            return Severity.WARNING_NORMAL;
        }
        return Severity.WARNING_LOW;
    }
}

