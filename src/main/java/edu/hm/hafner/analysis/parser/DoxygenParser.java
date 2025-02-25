package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for Doxygen.
 *
 * @author Ladislav Moravek
 */
public class DoxygenParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 8760302999081711502L;

    private static final String DOXYGEN_WARNING_PATTERN =
            ANT_TASK + "(?:(?:(.+?):(\\d+):|(.+?)\\((\\d+)\\):)?(?:(\\d+):)?)? ?([wW]arning|[Ee]rror): (.*)$";

    /**
     * Creates a new instance of {@link DoxygenParser}.
     */
    public DoxygenParser() {
        super(DOXYGEN_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return (line.contains("arning") || line.contains("rror")) && !line.contains("[javac]");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
                                          final IssueBuilder builder) {
        var message = new StringBuilder(matcher.group(7));

        if ((matcher.group(1) != null) || (matcher.group(2) != null)) {
            builder.setFileName(matcher.group(1));
            builder.setLineStart(matcher.group(2));
        }
        else {
            builder.setFileName(matcher.group(3));
            builder.setLineStart(matcher.group(4));
        }

        while (lookahead.hasNext() && Gcc4CompilerParser.isMessageContinuation(lookahead)) {
            message.append('\n');
            message.append(lookahead.next());
        }

        return builder
                .setColumnStart(matcher.group(5))
                .setMessage(message.toString())
                .setSeverity(Severity.guessFromString(matcher.group(6)))
                .buildOptional();
    }
}
