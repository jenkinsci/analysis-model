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
 * A parser for GCC cc1/cc1plus internal compiler warnings and errors.
 * These messages do not include file names or line numbers and are emitted by
 * the compiler's internal phases.
 *
 * @author Akash Manna
 * @see <a href="https://issues.jenkins.io/browse/JENKINS-73509">Issue 73509</a>
 */
public class Gcc4Cc1Parser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String GCC_CC1_WARNING_PATTERN = "^(?:In .+?:\\s*)?"
            + "(?<compiler>cc1(?:plus)?):\\s*"
            + "(?<severity>warning|error|note):\\s*"
            + "(?<message>.*)$";

    /**
     * Creates a new instance of {@link Gcc4Cc1Parser}.
     */
    public Gcc4Cc1Parser() {
        super(GCC_CC1_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String compilerName = matcher.group("compiler");
        String severityLevel = matcher.group("severity");
        String messageContent = matcher.group("message");

        if (StringUtils.isBlank(compilerName) || StringUtils.isBlank(severityLevel)
                || StringUtils.isBlank(messageContent)) {
            return Optional.empty();
        }

        var completeMessage = new StringBuilder(messageContent);
        while (lookahead.hasNext() && Gcc4CompilerParser.isMessageContinuation(lookahead, false)) {
            completeMessage.append(' ');
            completeMessage.append(lookahead.next());
        }

        String message = completeMessage.toString();
        String category = extractCategory(message);

        return builder.setFileName("-")
                .setLineStart(0)
                .setCategory(category)
                .setMessage(StringEscapeUtils.escapeXml10(message))
                .setSeverity(Severity.guessFromString(severityLevel))
                .buildOptional();
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("warning") || line.contains("error") || line.contains("note");
    }

    private String extractCategory(final String message) {
        int start = message.lastIndexOf("[-W");
        if (start == -1) {
            return "GCC warning";
        }

        int end = message.indexOf(']', start);
        if (end == -1) {
            return "GCC warning";
        }

        String category = message.substring(start + 3, end);
        int equalsPos = category.indexOf('=');
        if (equalsPos != -1) {
            return category.substring(0, equalsPos);
        }
        return category;
    }
}
