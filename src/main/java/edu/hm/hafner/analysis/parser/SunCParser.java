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
 * A parser for the SUN Studio C++ compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class SunCParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -1251248150596418456L;

    private static final String SUN_CPP_WARNING_PATTERN = "^\\s*\"(.*)\"\\s*,\\s*line\\s*(\\d+)\\s*:\\s*"
            + "(Warning|Error)(?:| .Anachronism.)\\s*(?:, \\s*([^:]*))?\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of {@link SunCParser}.
     */
    public SunCParser() {
        super(SUN_CPP_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(4))
                .setMessage(matcher.group(5))
                .setSeverity(mapPriority(matcher))
                .buildOptional();
    }

    private Severity mapPriority(final Matcher matcher) {
        if (equalsIgnoreCase(matcher.group(3), "warning")) {
            return Severity.WARNING_NORMAL;
        }
        else {
            return Severity.WARNING_HIGH;
        }
    }
}
