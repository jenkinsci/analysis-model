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
 * Parser for ProtoLint plaintext reports.
 *
 * <p>The recommended report format is now JSON! This parser is just used as fallback.
 *
 * @author David Hart
 * @see <a href="https://github.com/yoheimuta/protolint">https://github.com/yoheimuta/protolint</a>
 */
public class ProtoLintParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -8347619672754062010L;

    private static final String PROTO_LINT_PATTERN = "^\\[(?<file>[^:]+):(?<line>\\d+):(?<column>\\d+)\\] (?<message>.+)$";

    /**
     * Creates a new instance.
     */
    public ProtoLintParser() {
        super(PROTO_LINT_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"))
                .setColumnStart(matcher.group("column"))
                .setMessage(matcher.group("message"))
                .setSeverity(Severity.WARNING_NORMAL)
                .buildOptional();
    }
}
