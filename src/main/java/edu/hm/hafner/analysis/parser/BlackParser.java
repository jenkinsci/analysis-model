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
 * A parser for Black (Python code formatter) output.
 *
 * @author Akash Manna
 * @see <a href="https://black.readthedocs.io/">Black documentation</a>
 * @see <a href="https://github.com/psf/black">Black on GitHub</a>
 */
public class BlackParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -8266015919240804585L;

    private static final String BLACK_WARNING_PATTERN =
            "(?:error: cannot format (?<file>.+): Cannot parse: (?<line>\\d+):(?<col>\\d+): (?<msg>.+)"
            + "|(?<action>would reformat|reformatted) (?<rfile>.+))";

    /**
     * Creates a new instance of {@link BlackParser}.
     */
    public BlackParser() {
        super(BLACK_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.startsWith("error: cannot format") || line.startsWith("would reformat")
                || line.startsWith("reformatted");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        if (matcher.group("file") != null) {
            return builder
                    .setFileName(matcher.group("file"))
                    .setLineStart(matcher.group("line"))
                    .setColumnStart(matcher.group("col"))
                    .setMessage(matcher.group("msg"))
                    .setCategory("syntax-error")
                    .setSeverity(Severity.ERROR)
                    .buildOptional();
        }
        else {
            var action = matcher.group("action");
            var fileName = matcher.group("rfile");
            return builder
                    .setFileName(fileName)
                    .setMessage("\"" + fileName + "\" " + action + " by black")
                    .setCategory("formatting")
                    .setSeverity(Severity.WARNING_NORMAL)
                    .buildOptional();
        }
    }
}
