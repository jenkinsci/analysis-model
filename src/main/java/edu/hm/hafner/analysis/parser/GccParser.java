package edu.hm.hafner.analysis.parser;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = 2020182274225690532L;

    static final String GCC_ERROR = "GCC error";
    static final String LINKER_ERROR = "Linker error";
    private static final String GCC_WARNING_PATTERN = "^(?:\\s*(?:\\[.*\\]\\s*)?([^ ]*\\.[chpimxsola0-9]+):(?:(\\d*):"
            + "(?:\\d*:)*\\s*(?:(warning|error|note)\\s*:|\\s*(.*))|\\s*(undefined reference to.*))(.*)|.*ld:\\s*(.*-l("
            + ".*))|(?:In .+?:\\s*)?(cc1(?:plus)?):\\s*(warning|error|note):\\s*(.*))$";

    /**
     * Creates a new instance of {@link GccParser}.
     */
    public GccParser() {
        super(GCC_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        // Handle cc1/cc1plus warnings
        if (StringUtils.isNotBlank(matcher.group(9))) {
            Severity priority;
            if (equalsIgnoreCase(matcher.group(10), "warning")) {
                priority = Severity.WARNING_NORMAL;
            } 
            else if (equalsIgnoreCase(matcher.group(10), "error")) {
                priority = Severity.WARNING_HIGH;
            } 
            else if (equalsIgnoreCase(matcher.group(10), "note")) {
                priority = Severity.WARNING_LOW;
            } 
            else {
                priority = Severity.WARNING_NORMAL;
            }

            // Handle multi-line messages for cc1/cc1plus warnings
            var message = new StringBuilder(matcher.group(11));
            while (lookahead.hasNext() && isCc1MessageContinuation(lookahead)) {
                message.append(' ');
                message.append(lookahead.next());
            }

            return builder.setFileName("")
                    .setLineStart(0)
                    .setCategory("GCC " + matcher.group(10))
                    .setMessage(StringEscapeUtils.escapeXml10(message.toString()))
                    .setSeverity(priority)
                    .buildOptional();
        }

        if (StringUtils.isNotBlank(matcher.group(7))) {
            return builder.setFileName(matcher.group(8))
                    .setLineStart(0)
                    .setCategory(LINKER_ERROR)
                    .setMessage(matcher.group(7))
                    .setSeverity(Severity.WARNING_HIGH)
                    .buildOptional();
        }

        var fileName = matcher.group(1);
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

    /**
     * Determines if the next line is a continuation of a cc1/cc1plus warning
     * message.
     *
     * @param lookahead the lookahead stream
     * @return true if the next line is a continuation
     */
    private boolean isCc1MessageContinuation(final LookaheadStream lookahead) {
        var peek = lookahead.peekNext();
        if (peek.length() < 3) {
            return false;
        }
        // Don't continue if the line starts with common patterns that indicate a new
        // message
        if (peek.charAt(0) == '/' || peek.charAt(0) == '[' || peek.charAt(0) == '<' || peek.charAt(0) == '=') {
            return false;
        }
        if (peek.charAt(1) == ':') {
            return false;
        }
        if (peek.charAt(2) == '/' || peek.charAt(0) == '\\') {
            return false;
        }
        // Don't continue if the line contains patterns that indicate a new message
        return !StringUtils.containsAnyIgnoreCase(peek, "arning", "rror", "make", "cc1", "cc1plus",
                "In member function");
    }
}
