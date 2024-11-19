package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A parser for IBM xlC compiler warnings.
 *
 * @author Andrew Gvozdev
 */
@SuppressFBWarnings("REDOS")
public class XlcCompilerParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 5490211629355204910L;

    private static final String XLC_WARNING_PATTERN = ANT_TASK + ".*((?:[A-Z]+|[0-9]+-)[0-9]+)* ?\\([USEWI]\\)\\s*("
            + ".*)$";
    private static final String XLC_WARNING_PATTERN_WITH_LINE = ANT_TASK + "\"?([^\"]*)\"?, line ([0-9]+)\\.[0-9]+:( "
            + "(?:[A-Z]+|[0-9]+-)[0-9]+)? \\(([USEWI])\\)\\s*(.*)$";
    private static final String XLC_WARNING_PATTERN_NO_LINE = ANT_TASK + "\\s*((?:[A-Z]+|[0-9]+-)[0-9]+)?:? ?\\("
            + "([USEWI])\\)( INFORMATION:)?\\s*(.*)$";
    private static final Pattern PATTERN_WITH_LINE = Pattern.compile(XLC_WARNING_PATTERN_WITH_LINE);
    private static final Pattern PATTERN_WITHOUT_LINE = Pattern.compile(XLC_WARNING_PATTERN_NO_LINE);

    /**
     * Creates a new instance of {@link XlcCompilerParser}.
     */
    public XlcCompilerParser() {
        super(XLC_WARNING_PATTERN);
    }

    private Severity toPriority(final String severity) {
        switch (severity.charAt(0)) {
            case 'U':
            case 'S':
            case 'E':
                return Severity.WARNING_HIGH;
            case 'W':
                return Severity.WARNING_NORMAL;
            case 'I':
                return Severity.WARNING_LOW;
            default:
                return Severity.WARNING_HIGH;
        }
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        var line = matcher.group(0);

        var lineMatcher = PATTERN_WITH_LINE.matcher(line);
        if (lineMatcher.find()) {
            return builder.setFileName(lineMatcher.group(1))
                    .setLineStart(lineMatcher.group(2))
                    .setCategory(lineMatcher.group(3).trim())
                    .setMessage(lineMatcher.group(5))
                    .setSeverity(toPriority(lineMatcher.group(4)))
                    .buildOptional();
        }

        return createIssueWithoutLine(builder, line);
    }

    private Optional<Issue> createIssueWithoutLine(final IssueBuilder builder, final String line) {
        var matcher = PATTERN_WITHOUT_LINE.matcher(line);
        if (matcher.find()) {
            return builder.setFileName("")
                    .setLineStart(0)
                    .setCategory(matcher.group(1).trim())
                    .setMessage(matcher.group(4))
                    .setSeverity(toPriority(matcher.group(2)))
                    .buildOptional();
        }
        return Optional.empty();
    }
}
