package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * A parser for TASKING VX compiler warnings.
 *
 * @author Sven Lübke
 */
public class TaskingVxCompilerParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -5225265084645449716L;

    /** Pattern of TASKING VX compiler warnings. */
    private static final String TASKING_VX_COMPILER_WARNING_PATTERN = "^[a-z0-9]+? (I|W|E|F)(\\d+): (?:\\[\"(.*?)\" (\\d+)"
            + "\\/(\\d+)\\] )?(.*)$";

    /**
     * Creates a new instance of {@code TaskingVXCompilerParser}.
     */
    public TaskingVxCompilerParser() {
        super(TASKING_VX_COMPILER_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        var type = matcher.group(1);

        switch (type) {
            case "F" -> builder.setSeverity(Severity.ERROR);
            case "E" -> builder.setSeverity(Severity.WARNING_HIGH);
            case "I" -> builder.setSeverity(Severity.WARNING_LOW);
            default -> builder.setSeverity(Severity.WARNING_NORMAL);
        }

        return builder.setFileName(matcher.group(3))
                .setLineStart(matcher.group(4))
                .setMessage(matcher.group(6))
                .buildOptional();
    }
}
