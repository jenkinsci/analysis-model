package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.Strings;

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
 * A parser for gcc 4.x compiler warnings.
 *
 * @author Frederic Chateau
 */
public class Gcc4CompilerParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 5490211629355204910L;

    private static final String GCC_WARNING_PATTERN =
            ANT_TASK + "(.+?):(\\d+):(?:(\\d+):)? ?([wW]arning|.*[Ee]rror|[Nn]ote): (.*)$";
    private static final Pattern CLASS_PATTERN = Pattern.compile("\\[-W(.+)]$");
    private static final Pattern GCC_OPTION_PATTERN = Pattern.compile("\\[-(f[^\\]]+)]$");
    private static final Pattern CLANG_TIDY_PATTERN = Pattern.compile("\\[[^\\s\\]]+\\]$");
    private static final Pattern NOTE_PATTERN = Pattern.compile("^(?<file>.+?):(?<line>\\d+):(?:(?<column>\\d+):)? ?[Nn]ote: (?<message>.*)$");

    /**
     * Creates a new instance of {@link Gcc4CompilerParser}.
     */
    public Gcc4CompilerParser() {
        super(GCC_WARNING_PATTERN);
    }

    /**
     * Creates a new instance of {@link Gcc4CompilerParser} with specified pattern.
     * @param pattern a regex pattern to be used instead of the default one
     */
    Gcc4CompilerParser(final String pattern) {
        super(pattern);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return (line.contains("arning") || line.contains("rror") || line.contains("note:")) && !line.contains("[javac]");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        // Skip note lines - they will be attached to the previous warning/error
        if (matcher.group(4).equalsIgnoreCase("note")) {
            return Optional.empty();
        }

        var message = new StringBuilder(matcher.group(5));
        var originalMessage = message.toString();

        var classMatcher = CLASS_PATTERN.matcher(originalMessage);
        if (classMatcher.find() && classMatcher.group(1) != null) {
            builder.setCategory(classMatcher.group(1));
        }
        else {
            // Check for GCC compiler options like [-fpermissive]
            var optionMatcher = GCC_OPTION_PATTERN.matcher(originalMessage);
            if (optionMatcher.find() && optionMatcher.group(1) != null) {
                builder.setCategory(optionMatcher.group(1));
            }
        }

        while (lookahead.hasNext() && isMessageContinuation(lookahead)) {
            message.append('\n');
            message.append(lookahead.next());
        }

        var notes = getNotes(lookahead);
        if (!notes.isEmpty()) {
            message.append('\n');
            message.append(notes);
        }

        // Reject clang-tidy warnings that have [check-name] but not [-Wwarning-name]
        // clang-tidy warnings should be handled by ClangTidyParser
        var messageStr = message.toString();
        if (CLANG_TIDY_PATTERN.matcher(messageStr).find() && !CLASS_PATTERN.matcher(messageStr).find()) {
            return Optional.empty();
        }

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setColumnStart(matcher.group(3))
                .setMessage(messageStr)
                .setSeverity(Severity.guessFromString(matcher.group(4)))
                .buildOptional();
    }

    /**
     * Collects any subsequent note lines from the lookahead stream.
     *
     * @param lookahead the lookahead stream
     * @return a string containing all collected note lines, or an empty string if no notes were found
     */
    private String getNotes(final LookaheadStream lookahead) {
        var notes = new StringBuilder();
        while (lookahead.hasNext()) {
            var nextLine = lookahead.peekNext();
            var noteMatcher = NOTE_PATTERN.matcher(nextLine);
            if (noteMatcher.matches()) {
                if (!notes.isEmpty()) {
                    notes.append('\n');
                }
                notes.append(lookahead.next());
            }
            else {
                break;
            }
        }
        return notes.toString();
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    static boolean isMessageContinuation(final LookaheadStream lookahead) {
        var peek = lookahead.peekNext();
        if (peek.length() < 3) {
            return false;
        }
        if (peek.charAt(0) == '/' || peek.charAt(0) == '[' || peek.charAt(0) == '<' || peek.charAt(0) == '=') {
            return false;
        }
        if (peek.charAt(1) == ':') {
            return false;
        }
        if (peek.charAt(2) == '/' || peek.charAt(0) == '\\') {
            return false;
        }
        return !Strings.CI.containsAny(peek, "arning", "rror", "make");
    }
}
