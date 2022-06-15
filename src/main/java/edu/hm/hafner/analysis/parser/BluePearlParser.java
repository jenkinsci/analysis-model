package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;
import edu.hm.hafner.analysis.Severity;


/**
 * Parser for BluePearl Software Visual Verification Suite.
 *
 * @author Simon Matthews
 */
public class BluePearlParser extends LookaheadParser {
    public static final long serialVersionUID = 1L;

    /**
     * Matches the beginning of a Blue Pearl message".
     */
    private static final String BLUEPEARL_MESSAGE_REGEX = "^(?<severity>[WEF])-(?<type>\\w{3,4})-(?<messageNo>\\d{4}): ((?<filename>[a-zA-Z-0-9._/:\\\\]+)\\(?(?<lineNo>\\d+)?\\):)? *(?<messageString>.*)";

    /**
     * Creates a parser for BluePearlSoftware logs.
     */
    public BluePearlParser() {
        super(BLUEPEARL_MESSAGE_REGEX);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
                                          final IssueBuilder builder) {

        switch (matcher.group("severity")) {
            case "W": builder.setSeverity(Severity.WARNING_NORMAL);
                break;
            case "E": builder.setSeverity(Severity.ERROR);
                break;
            case "F": builder.setSeverity(Severity.ERROR);
                break;
        }
        builder.setMessage(matcher.group("messageString"));
        builder.setFileName(matcher.group("filename"));
        builder.setLineStart(matcher.group("lineNo"));
        builder.setCategory(matcher.group("type"));

        return builder.buildOptional();
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.startsWith("W-") || line.startsWith("E-") || line.startsWith("F-");
    }
}
