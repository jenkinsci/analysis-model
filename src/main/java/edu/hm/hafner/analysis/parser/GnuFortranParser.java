package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.RegexpDocumentParser;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for (compile-time) messages from the GNU Fortran Compiler.
 *
 * @author Mat Cross.
 */
public class GnuFortranParser extends LookaheadParser {
    private static final long serialVersionUID = 0L;


    /**
     * The gfortran regex string that follows has been reverse engineered from the show_locus function in
     * gcc/fortran/error.c at r204295. By inspection of the GCC release branches this regex should be compatible with
     * GCC 4.2 and newer.
     */
    private static final String MESSAGE_START_PATTERN = "(?s)^(.+\\.[^:]+):(\\d+)" // file:line
        + "(?:\\.(\\d+)(?:-(\\d+))?)?:"; // Optional coulm and range followed by ':'

    /** Optional part of the category */
    private static final Pattern LINE_PATTERN = Pattern.compile(" at \\(\\d\\)");
    /** Include lines between message start and end */
    private static final Pattern INCLUDE_LINE_PATTERN = Pattern.compile("(?:    Included at .+)");
    /** Simple regex to match any non empty lines which are required before the message end */
    private static final Pattern NON_EMPTY_LINE_PATTERN = Pattern.compile(".+");
    /** Regex to match the category and the actual error message itself */
    private static final Pattern ERROR_MESSAGE_PATTERN = Pattern.compile("(Warning|Error|Fatal Error|Internal Error at \\(1\\)):\\s?(.*)");

    /**
     * Creates a new instance of {@link GnuFortranParser}.
     */
    public GnuFortranParser() {
        super(MESSAGE_START_PATTERN);
    }

    /**
     * This Function is called once the start of a possible error message is detected.
     * It uses the provided lookaheadStream to check if the following lines match the message too.
     * The lines from the lookaheadStream are only consumed if they are successfully matched to a part
     * of the error message, if they can not be matched the function returns without creating an issue.
     *
     * @param matcher the regular expression matcher
     * @param lookahead the lookahead stream to read additional lines
     * @param builder the issue builder to use
     *
     * @return Issue if the rest of the message was sucesfully matched too
     */
    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead, final IssueBuilder builder) {
        // Gather location of the error
        String fileName = matcher.group(1);
        String lineStart = matcher.group(2);
        String columStart = matcher.group(3);
        String columEnd = matcher.group(4);

        if (!lookahead.hasNext()) {
            // This does not match the whole message, return without issue
            return Optional.empty();
        }
        String currentLine = lookahead.peekNext();

        // Match any number of include lines
        while (INCLUDE_LINE_PATTERN.matcher(currentLine).matches()) {
            lookahead.next(); // Only consume line if it matched
            if (!lookahead.hasNext()) {
                return Optional.empty();
            }
            currentLine = lookahead.peekNext();
        }

        // Optional include lines are followed by one empty line
        if (!currentLine.isEmpty()) {
            return Optional.empty();
        }
        lookahead.next(); // Consume the empty line

        // Check for two non empty lines now, one for the offending line one for a numbered indicator
        if (!lookahead.hasNext() || !NON_EMPTY_LINE_PATTERN.matcher(lookahead.peekNext()).matches()) {
            return Optional.empty();
        }
        lookahead.next(); // Consume after match

        if (!lookahead.hasNext() || !NON_EMPTY_LINE_PATTERN.matcher(lookahead.peekNext()).matches()) {
            return Optional.empty();
        }
        lookahead.next(); // Consume after match

        if (!lookahead.hasNext()) {
            // Missing error message line
            return Optional.empty();
        }
        String errrorMessageLine = lookahead.peekNext();
        Matcher messageMatcher = ERROR_MESSAGE_PATTERN.matcher(errrorMessageLine);
        if (!messageMatcher.matches()) {
            // Invalid message line
            return Optional.empty();
        }
        lookahead.next();

        // Get the category (warning, error, ...) and message and trim location references
        String category = LINE_PATTERN.matcher(messageMatcher.group(1)).replaceAll("");
        String message = LINE_PATTERN.matcher(messageMatcher.group(2)).replaceAll("");

        // If the message was not directly in the previous line, interpret the last line of the matched text as message
        if (message.isEmpty() && lookahead.hasNext()) {
            message = LINE_PATTERN.matcher(lookahead.next()).replaceAll("");
        }

        return builder.setFileName(fileName)
                .setColumnStart(columStart)
                .setColumnEnd(columEnd)
                .setLineStart(lineStart)
                .setCategory(category)
                .setMessage(message)
                .setSeverity(Severity.guessFromString(category))
                .buildOptional();
    }
}
