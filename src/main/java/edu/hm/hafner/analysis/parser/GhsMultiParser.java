package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // Regex Pattern to match start of Warning / Error
    // Groups are used to identify FileName, StartLine, Type, Category, Start of message
    private static final String GHS_MULTI_WARNING_PATTERN =
            "(?:\\.|[A-Z]:)(.*)\"\\,\\s*line\\s*(\\d+):\\s*(warning|error)\\s*([^:]+):\\s*(?m)([^\\^]*)";

    // Regex Pattern to match the ending of the Warning / Error Message
    private static final Pattern MESSAGE_END_PATTERN = Pattern.compile("\\s*\\^");


    /**
     * Creates a new instance of {@link GhsMultiParser}.
     */
    public GhsMultiParser() {
        super(GHS_MULTI_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead, final IssueBuilder builder) {
        String type = StringUtils.capitalize(matcher.group(3));
        StringBuilder messageBuilder = new StringBuilder(matcher.group(5)).append("\n");

        // Go through all following lines appending the message until a line with only the ^ Symbol is found
        boolean messageEndFound = false;
        while (!messageEndFound && lookahead.hasNext()) {
            String messageLine = lookahead.next();
            Matcher messageEndMatcher = MESSAGE_END_PATTERN.matcher(messageLine);
            if (messageEndMatcher.matches()) {
                messageEndFound = true;
            }
            else {
                messageBuilder.append(messageLine).append("\n");
            }
        }

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(4))
                .setMessage(messageBuilder.toString())
                .setSeverity(Severity.guessFromString(type))
                .buildOptional();
    }
}

