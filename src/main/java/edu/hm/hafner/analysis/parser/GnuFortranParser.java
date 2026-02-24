package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser for (compile-time) messages from the GNU Fortran Compiler.
 *
 * @author Mat Cross.
 */
public class GnuFortranParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -1473951397615098271L;

    /**
     * The gfortran regex string that follows has been reverse-engineered from the show_locus function in
     * gcc/fortran/error.c at r204295. By inspection of the GCC release branches, this regex should be compatible with
     * GCC 4.2 and newer.
     */
    private static final String MESSAGE_START_REGEX = "(?s)^(.+\\.[^:]+):(\\d+)(?:\\.(\\d+)(?:-(\\d+))?)?:";
    // file:file followed by Optional coulm and range followed by a colon.

    /** Include lines between message start and end. */
    private static final String INCLUDE_LINE_REGEX = "(?: {4}Included at .+)";
    /** Simple regex to match any non empty lines which are required before the message end. */
    private static final String NON_EMPTY_LINE_REGEX = ".+";
    /** Simple regex to match any lines which are completely empty. */
    private static final String EMPTY_LINE_REGEX = "^$";
    /** Optional part of the category. */
    private static final Pattern MESSAGE_TRIM_PATTERN = Pattern.compile(" at \\(\\d\\)");
    /** Regex to match the category and the actual error message itself. */
    private static final Pattern ERROR_MESSAGE_PATTERN = Pattern.compile("(Warning|Error|Fatal Error|Internal Error at \\(1\\)):\\s?(.*)");

    /**
     * Creates a new instance of {@link GnuFortranParser}.
     */
    public GnuFortranParser() {
        super(MESSAGE_START_REGEX);
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
        // Gather location of the error.

        // Match all include lines
        while (lookahead.hasNext(INCLUDE_LINE_REGEX)) {
            lookahead.next();
        }

        // Optional include lines are followed by one empty line.
        if (!lookahead.hasNext(EMPTY_LINE_REGEX)) {
            return Optional.empty();
        }
        lookahead.next(); // Consume the empty line.

        // Check for two non empty lines now, one for the offending line one for a numbered indicator.
        if (!lookahead.hasNext(NON_EMPTY_LINE_REGEX)) {
            return Optional.empty();
        }
        lookahead.next(); // Consume after match.

        if (!lookahead.hasNext(NON_EMPTY_LINE_REGEX)) {
            return Optional.empty();
        }
        lookahead.next(); // Consume after match.

        if (!lookahead.hasNext()) {
            // Missing error message line.
            return Optional.empty();
        }

        var errorMessageLine = lookahead.peekNext();
        var messageMatcher = ERROR_MESSAGE_PATTERN.matcher(errorMessageLine);
        if (!messageMatcher.matches()) {
            // Invalid message line.
            return Optional.empty();
        }
        lookahead.next();

        // Get the category (warning, error, ...) and message and trim location references.
        var category = MESSAGE_TRIM_PATTERN.matcher(messageMatcher.group(1)).replaceAll("");
        var message = MESSAGE_TRIM_PATTERN.matcher(messageMatcher.group(2)).replaceAll("");

        // If the message was not directly in the previous line, interpret the last line of the matched text as message.
        if (message.isEmpty() && lookahead.hasNext()) {
            message = MESSAGE_TRIM_PATTERN.matcher(lookahead.next()).replaceAll("");
        }

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setColumnStart(matcher.group(3))
                .setColumnEnd(matcher.group(4))
                .setCategory(category)
                .setMessage(message)
                .setSeverity(Severity.guessFromString(category))
                .buildOptional();
    }
}
