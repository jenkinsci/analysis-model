package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the gcc compiler warnings.
 *
 * @author Greg Roth
 */
public class GccParser extends LookaheadParser {
    private static final long serialVersionUID = 2020182274225690532L;

    static final String GCC_ERROR = "GCC error";
    static final String LINKER_ERROR = "Linker error";
    private static final String GCC_WARNING_PATTERN = "^(?:\\s*(?:\\[.*\\]\\s*)?([^ ]*\\.[chpimxsola0-9]+):(?:(\\d*):"
            + "(?:\\d*:)*\\s*(?:(warning|error|note)\\s*:|\\s*(.*))|\\s*(undefined reference to.*))(.*)|.*ld:\\s*(.*-l("
            + ".*)))$";

    /**
     * Creates a new instance of {@link GccParser}.
     */
    public GccParser() {
        super(GCC_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        if (StringUtils.isNotBlank(matcher.group(7))) {
            return builder.setFileName(matcher.group(8))
                    .setLineStart(0)
                    .setCategory(LINKER_ERROR)
                    .setMessage(matcher.group(7))
                    .setSeverity(Severity.WARNING_HIGH)
                    .buildOptional();
        }

        String fileName = matcher.group(1);
        if (StringUtils.contains(fileName, "cleartool")) {
            return Optional.empty();
        }

        Severity priority;
        if (equalsIgnoreCase(matcher.group(3), "warning")) {
            priority = Severity.WARNING_NORMAL;
        }
        else if (equalsIgnoreCase(matcher.group(3), "error")) {
            priority = Severity.WARNING_HIGH;
        }
        else if (equalsIgnoreCase(matcher.group(3), "note")) {
            priority = Severity.WARNING_LOW;
        }
        else if (StringUtils.isNotBlank(matcher.group(4))) {
            if (matcher.group(4).contains("instantiated from here")) {
                return Optional.empty();
            }
            return builder.setFileName(fileName)
                    .setLineStart(matcher.group(2))
                    .setCategory(GCC_ERROR)
                    .setMessage(StringEscapeUtils.escapeXml10(matcher.group(4)))
                    .setSeverity(Severity.WARNING_HIGH)
                    .buildOptional();
        }
        else {
            return builder.setFileName(fileName)
                    .setLineStart(0)
                    .setCategory(GCC_ERROR)
                    .setMessage(StringEscapeUtils.escapeXml10(matcher.group(5)))
                    .setSeverity(Severity.WARNING_HIGH)
                    .buildOptional();
        }
        return builder.setFileName(fileName)
                .setLineStart(matcher.group(2))
                .setCategory("GCC " + matcher.group(3))
                .setMessage(StringEscapeUtils.escapeXml10(matcher.group(6)))
                .setSeverity(priority)
                .buildOptional();
    }
}

