package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.analysis.Categories.*;

/**
 * A parser for the javac compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class JavacParser extends LookaheadParser {
    private static final long serialVersionUID = 7199325311690082782L;

    private static final String ERRORPRONE_URL_PATTERN = "\\s+\\(see https?://errorprone\\S+\\s*\\)";

    private static final String JAVAC_WARNING_PATTERN
            = "^(?:\\S+\\s+)?"                // optional preceding arbitrary number of characters that are not a
                                              // whitespace followed by whitespace. This can be used for timestamps.
            + "(?:(?:\\[(WARNING|ERROR)\\]|w:)\\s+)" // optional [WARNING] or [ERROR] or w:
            + "([^\\[\\(]*):\\s*"             // group 1: filename
            + "[\\[\\(]"                      // [ or (
            + "(\\d+)[.,;]*"                  // group 2: line number
            + "\\s?(\\d+)?"                   // group 3: optional column
            + "[\\]\\)]\\s*"                  // ] or )
            + ":?"                            // optional :
            + "(?:\\[(\\w+)\\])?"             // group 4: optional category
            + "\\s*(.*)$";                    // group 5: message

    /**
     * Creates a new instance of {@link JavacParser}.
     */
    public JavacParser() {
        super(JAVAC_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[") || line.contains("w:");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) throws ParsingException {        
        if (lookahead.hasNext(ERRORPRONE_URL_PATTERN)) {
            return Optional.empty();
        }

        String type = matcher.group(1);
        if ("ERROR".equals(type)) {
            builder.setSeverity(Severity.ERROR);
        }
        else {
            builder.setSeverity(Severity.WARNING_NORMAL);
        }

        String message = matcher.group(6);
        String category = guessCategoryIfEmpty(matcher.group(5), message);

        return builder.setFileName(matcher.group(2))
                .setLineStart(matcher.group(3))
                .setColumnStart(matcher.group(4))
                .setCategory(category)
                .setMessage(message)
                .buildOptional();
    }
}

