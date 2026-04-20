package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.Strings;
import org.dom4j.util.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.util.IntegerParser;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.ArrayList;
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

    private static final String LOCATION = "(?<file>.+?):(?<line>\\d+):(?:(?<column>\\d+):)?";
    private static final String GCC_WARNING_PATTERN =
            ANT_TASK + LOCATION + " ?(?<severity>[wW]arning|.*[Ee]rror): (?<message>.*)$";
    private static final Pattern CLASS_PATTERN = Pattern.compile("\\[-W(.+)]$");
    private static final Pattern GCC_OPTION_PATTERN = Pattern.compile("\\[-(f[^\\]]+)]$");
    private static final Pattern CLANG_TIDY_PATTERN = Pattern.compile("\\[[^\\s\\]]+\\]$");
    private static final Pattern NOTE_PATTERN = Pattern.compile("^" + LOCATION + " ?[Nn]ote: (?<message>.*)$");

    /**
     * Creates a new instance of {@link Gcc4CompilerParser}.
     */
    public Gcc4CompilerParser() {
        super(GCC_WARNING_PATTERN);
    }

    /**
     * Creates a new instance of {@link Gcc4CompilerParser} with the specified pattern.
     *
     * @param pattern
     *         a regex pattern to be used instead of the default one
     */
    Gcc4CompilerParser(final String pattern) {
        super(pattern);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return (line.contains("arning") || line.contains("rror") || line.contains("ote:")) && !line.contains("[javac]");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        var originalMessage = matcher.group("message");
        if (isClangTidyWarning(originalMessage)) {
            return Optional.empty();
        }

        addLocation(matcher, builder);

        var message = new StringBuilder(originalMessage);
        boolean hasCodeSnippet = false;
        while (lookahead.hasNext() && isMessageContinuation(lookahead, hasCodeSnippet)) {
            var continuation = lookahead.next();
            if (StringUtils.startsWithWhitespace(continuation)) {
                hasCodeSnippet = true;
            }
            message.append('\n');
            message.append(continuation);
        }

        var notes = collectNotes(lookahead, builder);
        if (!notes.isEmpty()) {
            message.append('\n');
            message.append(notes);
        }

        setCategory(builder, originalMessage);
        return builder.setMessage(message)
                .guessSeverity(matcher.group("severity"))
                .buildOptional();
    }

    private void addLocation(final Matcher matcher, final IssueBuilder builder) {
        var fileName = matcher.group("file");
        var line = toInt(matcher.group("line"));
        var column = toInt(matcher.group("column"));

        builder.addLocation(fileName, line, line, column, column);
    }

    private int toInt(final String number) {
        return IntegerParser.parseInt(number);
    }

    /**
     * Check if the warning is from clang-tidy. Those warnings are quite similar and have [check-name] but not
     * [-Wwarning-name] or [-fcompiler-option]. Since a separate parser handles clang-tidy warnings, we can skip them
     * here.
     *
     * @param message
     *         the original message to check
     *
     * @return {@code true} if the message is a clang-tidy warning, {@code false} otherwise
     */
    private boolean isClangTidyWarning(final String message) {
        return CLANG_TIDY_PATTERN.matcher(message).find()
                && !CLASS_PATTERN.matcher(message).find()
                && !GCC_OPTION_PATTERN.matcher(message).find();
    }

    /**
     * Sets the category based on the original message content. Checks for GCC warning categories (e.g.,
     * [-Wunused-variable]) or compiler options (e.g., [-fpermissive]).
     *
     * @param builder
     *         the issue builder
     * @param originalMessage
     *         the original message to extract the category from
     */
    private void setCategory(final IssueBuilder builder, final String originalMessage) {
        var classMatcher = CLASS_PATTERN.matcher(originalMessage);
        if (classMatcher.find() && classMatcher.group(1) != null) {
            builder.setCategory(classMatcher.group(1));
        }
        else {
            var optionMatcher = GCC_OPTION_PATTERN.matcher(originalMessage);
            if (optionMatcher.find() && optionMatcher.group(1) != null) {
                builder.setCategory(optionMatcher.group(1));
            }
        }
    }

    /**
     * Collects any further note lines from the lookahead stream and adds them as additional locations.
     *
     * @param lookahead
     *         the lookahead stream
     * @param builder
     *         the issue builder to add locations to
     *
     * @return a string containing all collected note lines, or an empty string if no notes were found
     */
    private String collectNotes(final LookaheadStream lookahead, final IssueBuilder builder) {
        var notes = new ArrayList<String>();
        while (lookahead.hasNext()) {
            var noteMatcher = NOTE_PATTERN.matcher(lookahead.peekNext());
            if (noteMatcher.matches()) {
                notes.add(lookahead.next());
                addLocation(noteMatcher, builder);
            }
            else {
                break;
            }
        }
        return String.join("\n", notes);
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    static boolean isMessageContinuation(final LookaheadStream lookahead, final boolean hasCodeSnippet) {
        var peek = lookahead.peekNext();
        if (peek.length() < 3) {
            return false;
        }
        return !startsWithInvalidCharacter(peek, hasCodeSnippet)
                && !Strings.CI.containsAny(peek, "arning", "rror", "make");
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    private static boolean startsWithInvalidCharacter(final String peek, final boolean hasCodeSnippet) {
        if (peek.charAt(0) == '/' || peek.charAt(0) == '[' || peek.charAt(0) == '<' || peek.charAt(0) == '=') {
            return true;
        }
        if (peek.charAt(1) == ':') {
            return true;
        }
        if (peek.charAt(2) == '/' || peek.charAt(0) == '\\') {
            return true;
        }
        return hasCodeSnippet && peek.startsWith("In file included from");
    }
}
