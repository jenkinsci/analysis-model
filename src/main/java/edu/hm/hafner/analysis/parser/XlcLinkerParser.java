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
 * A parser for IBM xlC linker warnings.
 *
 * @author Andrew Gvozdev
 */
@SuppressFBWarnings("REDOS")
public class XlcLinkerParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 211259620936831096L;
    private static final String XLC_LINKER_WARNING_PATTERN = ANT_TASK + "ld: ([0-9]+-[0-9]+)*\\s*(.*)$";

    private static final String XLC_LINKER_WARNING_PATTERN_ERROR_1 = ANT_TASK + "ld: ([0-9]+-[0-9]+).*ERROR:\\s*(.*)$";
    private static final String XLC_LINKER_WARNING_PATTERN_ERROR_2 = ANT_TASK + "ld: ([0-9]+-[0-9]+)\\s*(Error .*)$";
    private static final String XLC_LINKER_WARNING_PATTERN_WARNING = ANT_TASK + "ld: ([0-9]+-[0-9]+)\\s*WARNING:\\s*("
            + ".*)$";
    private static final String XLC_LINKER_WARNING_PATTERN_INFO = ANT_TASK + "ld: ([0-9]+-[0-9]+)\\s*(.*)$";
    private static final Pattern PATTERN_ERROR_1 = Pattern.compile(XLC_LINKER_WARNING_PATTERN_ERROR_1);
    private static final Pattern PATTERN_ERROR_2 = Pattern.compile(XLC_LINKER_WARNING_PATTERN_ERROR_2);
    private static final Pattern PATTERN_WARNING = Pattern.compile(XLC_LINKER_WARNING_PATTERN_WARNING);
    private static final Pattern PATTERN_INFO = Pattern.compile(XLC_LINKER_WARNING_PATTERN_INFO);

    /**
     * Creates a new instance of {@link XlcLinkerParser}. Note that the name matches {@link XlcCompilerParser} to unite
     * them as one parser in UI.
     */
    public XlcLinkerParser() {
        super(XLC_LINKER_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String line = matcher.group(0);
        builder.setFileName("").setLineStart(0);

        Matcher lineMatcher = PATTERN_ERROR_1.matcher(line);
        if (lineMatcher.find()) {
            String category = lineMatcher.group(1);
            String message = lineMatcher.group(2);
            return builder.setCategory(category).setMessage(message).setSeverity(Severity.WARNING_HIGH).buildOptional();
        }
        lineMatcher = PATTERN_ERROR_2.matcher(line);
        if (lineMatcher.find()) {
            String category = lineMatcher.group(1);
            String message = lineMatcher.group(2);
            return builder.setCategory(category).setMessage(message).setSeverity(Severity.WARNING_HIGH).buildOptional();
        }
        lineMatcher = PATTERN_WARNING.matcher(line);
        if (lineMatcher.find()) {
            String category = lineMatcher.group(1);
            String message = lineMatcher.group(2);
            return builder.setCategory(category)
                    .setMessage(message)
                    .setSeverity(Severity.WARNING_NORMAL)
                    .buildOptional();
        }
        lineMatcher = PATTERN_INFO.matcher(line);
        if (lineMatcher.find()) {
            String category = lineMatcher.group(1);
            String message = lineMatcher.group(2);
            return builder.setCategory(category).setMessage(message).setSeverity(Severity.WARNING_LOW).buildOptional();
        }
        return Optional.empty();
    }
}
