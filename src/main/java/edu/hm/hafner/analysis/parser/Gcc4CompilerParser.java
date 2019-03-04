package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;
import edu.hm.hafner.util.StringContainsUtils;

/**
 * A parser for gcc 4.x compiler warnings.
 *
 * @author Frederic Chateau
 */
public class Gcc4CompilerParser extends LookaheadParser {
    private static final long serialVersionUID = 5490211629355204910L;

    private static final String GCC_WARNING_PATTERN =
            ANT_TASK + "(.+?):(\\d+):(?:(\\d+):)? ?([wW]arning|.*[Ee]rror): (.*)$";
    private static final Pattern CLASS_PATTERN = Pattern.compile("\\[-W(.+)]$");

    /**
     * Creates a new instance of {@link Gcc4CompilerParser}.
     */
    public Gcc4CompilerParser() {
        super(GCC_WARNING_PATTERN);
    }

    @Override
    protected String interestingLineContent(String line) {
        if (line.contains("arning") || line.contains("rror")) {
            return line;
        }

        return null;
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        StringBuilder message = new StringBuilder(matcher.group(5));

        Matcher classMatcher = CLASS_PATTERN.matcher(message.toString());
        if (classMatcher.find() && classMatcher.group(1) != null) {
            builder.setCategory(classMatcher.group(1));
        }

        while (lookahead.hasNext() && isMessageContinuation(lookahead)) {
            message.append("\n");
            message.append(lookahead.next());
        }

        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setColumnStart(matcher.group(3))
                .setMessage(message.toString())
                .setSeverity(Severity.guessFromString(matcher.group(4)))
                .buildOptional();
    }

    private boolean isMessageContinuation(final LookaheadStream lookahead) {
        String peek = lookahead.peekNext();
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
        return !(StringContainsUtils.containsAnyIgnoreCase(peek, "arning", "rror", "make"));
    }
}

