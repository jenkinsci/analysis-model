package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;


import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the clang-tidy static analysis warnings parsed by Codechecker.
 *
 * Codechecker parses the *.plist files and converts it into plain text file.
 *
 * Better for human readers and for using grep and diff
 * It also puts the human-readable Severity at the start of a line.
 *
 */
public class CodeCheckerParser extends LookaheadParser {
    private static final long serialVersionUID = -3015592762345283582L;
    private static final String CODE_CHECKER_DEFECT_PATTERN =
            "^\\[(CRITICAL|HIGH|MEDIUM|LOW)\\] (.+):(\\d+):(\\d+): (.*?) \\[([^\\s]*?)\\]$";

    /**
     * Creates a new instance of {@link CodeCheckerParser}.
     */
    public CodeCheckerParser() {
        super(CODE_CHECKER_DEFECT_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        Severity priority;
        if (matcher.group(1).contains("CRITICAL")) {
            priority = Severity.ERROR;
        }
        else if (matcher.group(1).contains("HIGH")) {
            priority = Severity.WARNING_HIGH;
        }
        else if (matcher.group(1).contains("MEDIUM")) {
            priority = Severity.WARNING_NORMAL;
        }
        else {
            priority = Severity.WARNING_LOW;
        }
        return builder.setFileName(matcher.group(2))
                .setSeverity(priority)
                .setLineStart(matcher.group(3))
                .setColumnStart(matcher.group(4))
                .setCategory(matcher.group(6))
                .setMessage(matcher.group(5))
                .setType("Warning")
                .buildOptional();
    }
}
