package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the GHS Multi compiler warnings.
 *
 * @author Joseph Boulos
 */
public class GhsMultiParser extends LookaheadParser {
    private static final long serialVersionUID = 8149238560432255036L;

    /**
     * Regex Pattern to match start of Warning / Error. Groups are used to identify FileName, StartLine, Type, Category,
     * Start of message.
     */
    private static final String GHS_MULTI_WARNING_PATTERN =
            "\"(.*)\"\\,\\s*line\\s*(\\d+):\\s*(warning|error)\\s*([^:]+):\\s*(?m)([^\\^]*)";

    /** Regex Pattern to match the ending of the Warning / Error Message. */
    private static final String MESSAGE_END_REGEX = "\\s*\\^";

    /**
     * Creates a new instance of {@link GhsMultiParser}.
     */
    public GhsMultiParser() {
        super(GHS_MULTI_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String type = StringUtils.capitalize(matcher.group(3));
        String messageStart = matcher.group(5);

        String message = extractMessage(messageStart, lookahead);

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(4))
                .setMessage(message)
                .setSeverity(Severity.guessFromString(type))
                .buildOptional();
    }

    /**
     * Go through all following lines appending the message until a line with only the ^ Symbol is found.
     *
     * @param messageStart
     *         start of the message
     * @param lookahead
     *         lines used for message creation
     *
     * @return concatenated message string
     */
    private String extractMessage(final String messageStart, final LookaheadStream lookahead) {
        StringBuilder messageBuilder = new StringBuilder(messageStart).append("\n");

        while (!lookahead.hasNext(MESSAGE_END_REGEX) && lookahead.hasNext()) {
            messageBuilder.append(lookahead.next()).append("\n");
        }

        return messageBuilder.toString();
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("warning") || line.contains("error");
    }
}

