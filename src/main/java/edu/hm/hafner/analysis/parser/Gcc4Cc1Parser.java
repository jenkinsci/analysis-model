package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Locale;
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
 * A parser for GCC cc1/cc1plus internal compiler warnings and errors.
 * These messages do not include file names or line numbers and are emitted
 * by the compiler's internal phases.
 *
 * @author Akash Manna
 * @see <a href="https://issues.jenkins.io/browse/JENKINS-73509">Issue 73509</a>
 */
public class Gcc4Cc1Parser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String GCC_CC1_WARNING_PATTERN = "^(?:In .+?:\\s*)?(?<compiler>cc1(?:plus)?):\\s*(?<severity>warning|error|note):\\s*(?<message>.*)$";

    /**
     * Creates a new instance of {@link Gcc4Cc1Parser}.
     */
    public Gcc4Cc1Parser() {
        super(GCC_CC1_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String compiler = matcher.group("compiler");
        String severityString = matcher.group("severity");
        String message = matcher.group("message");

        if (StringUtils.isBlank(compiler) || StringUtils.isBlank(severityString) || StringUtils.isBlank(message)) {
            return Optional.empty();
        }

        Severity priority = mapSeverity(severityString);

        // Handle multi-line messages for cc1/cc1plus warnings
        var fullMessage = new StringBuilder(message);
        while (lookahead.hasNext() && isCc1MessageContinuation(lookahead)) {
            fullMessage.append(' ');
            fullMessage.append(lookahead.next());
        }

        return builder.setFileName("-")
                .setLineStart(0)
                .setCategory("GCC " + severityString)
                .setMessage(StringEscapeUtils.escapeXml10(fullMessage.toString()))
                .setSeverity(priority)
                .buildOptional();
    }

    /**
     * Maps the severity string to a Severity enum value.
     *
     * @param severityString the severity string from the compiler output
     * @return the corresponding Severity enum value
     */
    private Severity mapSeverity(final String severityString) {
        if (equalsIgnoreCase(severityString, "warning")) {
            return Severity.WARNING_NORMAL;
        } else if (equalsIgnoreCase(severityString, "error")) {
            return Severity.WARNING_HIGH;
        } else if (equalsIgnoreCase(severityString, "note")) {
            return Severity.WARNING_LOW;
        }
        return Severity.WARNING_NORMAL;
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
        String peekLower = peek.toLowerCase(Locale.ROOT);
        return !(peekLower.contains("arning")
                || peekLower.contains("rror")
                || peekLower.contains("make")
                || peekLower.contains("cc1")
                || peekLower.contains("in member function"));
    }
}
