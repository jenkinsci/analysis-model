package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for armcc5 compiler warnings.
 *
 * @author Dmytro Kutianskyi
 */
public class Armcc5CompilerParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -2677728927938443701L;

    private static final String ARMCC5_WARNING_PATTERN =
            "^(?:(?:\"(?<file1>.+)\", line (?<line1>\\d+): (?<severity1>warning|error) #(?<code1>.+): (?<message1>.+))" +  // Format for Armcc version < 5
            "|(?<file2>.+)\\((?<line2>\\d+)\\): (?<severity2>warning|error):\\s+#(?<code2>.+): (?<message2>.+))$";  // Format for Armcc version >= 5

    /**
     * Creates a new instance of {@link Armcc5CompilerParser}.
     */
    public Armcc5CompilerParser() {
        super(ARMCC5_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("#");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
                                          final IssueBuilder builder) {
        if (matcher.group("file1") != null) {
            var errorCode = matcher.group("code1");
            var message = matcher.group("message1");
            builder.setFileName(matcher.group("file1"));
            builder.setLineStart(matcher.group("line1"));
            builder.setMessage(errorCode + " - " + message);
            builder.setSeverity(Severity.guessFromString(matcher.group("severity1")));
        }
        else {
            var errorCode = matcher.group("code2");
            var message = matcher.group("message2");
            builder.setFileName(matcher.group("file2"));
            builder.setLineStart(matcher.group("line2"));
            builder.setMessage(errorCode + " - " + message);
            builder.setSeverity(Severity.guessFromString(matcher.group("severity2")));
        }

        return builder
                .buildOptional();
    }
}
