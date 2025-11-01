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
 * These messages do not include file names or line numbers and are emitted by the compiler's internal phases.
 *
 * @author Akash Manna
 * @see <a href="https://issues.jenkins.io/browse/JENKINS-73509">Issue 73509</a>
 */
public class Gcc4Cc1Parser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Pattern to match cc1/cc1plus compiler messages.
     * Named groups:
     * - compiler: The compiler name (cc1 or cc1plus)
     * - severity: The message severity (warning, error, or note)
     * - message: The actual warning/error message content
     */
    private static final String GCC_CC1_WARNING_PATTERN = "^(?:In .+?:\\s*)?" // Optional context prefix (e.g., "In member function:")
            + "(?<compiler>cc1(?:plus)?):\\s*" // Compiler name: cc1 or cc1plus
            + "(?<severity>warning|error|note):\\s*" // Severity level
            + "(?<message>.*)$"; // The actual message content

    /**
     * Creates a new instance of {@link Gcc4Cc1Parser}.
     */
    public Gcc4Cc1Parser() {
        super(GCC_CC1_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        // Extract named groups from the regex match
        String compilerName = matcher.group("compiler"); // cc1 or cc1plus
        String severityLevel = matcher.group("severity"); // warning, error, or note
        String messageContent = matcher.group("message"); // The actual message text

        // Validate that all required fields are present
        if (StringUtils.isBlank(compilerName) || StringUtils.isBlank(severityLevel)
                || StringUtils.isBlank(messageContent)) {
            return Optional.empty();
        }

        // Map the severity string to the appropriate Severity enum
        Severity issueSeverity = mapSeverity(severityLevel);

        // Build the complete message by appending continuation lines if present
        var completeMessage = new StringBuilder(messageContent);
        while (lookahead.hasNext() && isCc1MessageContinuation(lookahead)) {
            completeMessage.append(' ');
            completeMessage.append(lookahead.next());
        }

        // Build and return the issue with appropriate metadata
        return builder.setFileName("-") // cc1 messages don't have associated files
                .setLineStart(0) // cc1 messages don't have line numbers
                .setCategory("GCC " + severityLevel) // e.g., "GCC warning"
                .setMessage(StringEscapeUtils.escapeXml10(completeMessage.toString()))
                .setSeverity(issueSeverity)
                .buildOptional();
    }

    /**
     * Maps the severity string from compiler output to a Severity enum value.
     * 
     * Mapping:
     * - "warning" → WARNING_NORMAL
     * - "error" → WARNING_HIGH
     * - "note" → WARNING_LOW
     * - default → WARNING_NORMAL
     *
     * @param severityString the severity string from the compiler output
     *                       (warning/error/note)
     * @return the corresponding Severity enum value for issue classification
     */
    private Severity mapSeverity(final String severityString) {
        if (equalsIgnoreCase(severityString, "warning")) {
            return Severity.WARNING_NORMAL;
        } else if (equalsIgnoreCase(severityString, "error")) {
            return Severity.WARNING_HIGH;
        } else if (equalsIgnoreCase(severityString, "note")) {
            return Severity.WARNING_LOW;
        }
        return Severity.WARNING_NORMAL; // Default fallback
    }

    /**
     * Determines if the next line is a continuation of a cc1/cc1plus warning message.
     *
     * @param lookahead the lookahead stream
     * @return true if the next line is a continuation
     */
    private boolean isCc1MessageContinuation(final LookaheadStream lookahead) {
        var peek = lookahead.peekNext();
        if (peek.length() < 3) {
            return false;
        }
        // Don't continue if the line starts with common patterns that indicate a new message
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
