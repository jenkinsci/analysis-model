package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for Flex SDK compiler warnings.
 *
 * @author Vivien Tintillier
 */
public class FlexSdkParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -185055018399324311L;

    private static final String FLEX_SDK_WARNING_PATTERN = "^\\s*(?:\\[.*\\])?\\s*(.*\\.as|.*\\.mxml)\\((\\d*)\\)"
            + ":\\s*(?:col:\\s*\\d*\\s*)?(?:Warning)\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of {@link FlexSdkParser}.
     */
    public FlexSdkParser() {
        super(FLEX_SDK_WARNING_PATTERN);
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
                .buildOptional();
    }
}
