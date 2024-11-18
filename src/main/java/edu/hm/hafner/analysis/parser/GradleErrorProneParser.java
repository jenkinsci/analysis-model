package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.analysis.parser.ErrorProneParser.*;

/**
 * A parser for ErrorProne warnings during a Gradle build.
 *
 * @author Ullrich Hafner
 */
public class GradleErrorProneParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -3776472281369602440L;

    private static final String WARNINGS_PATTERN
            = "^(?<file>.+):"
            + "\\s*(?<line>\\d+)\\s*:"
            + "\\s*(?<severity>warning|error)\\s*:"
            + "\\s*\\[(?<type>\\w+)\\]\\s+"
            + "(?<message>.*)$";

    /**
     * Creates a new instance of {@link GradleErrorProneParser}.
     */
    public GradleErrorProneParser() {
        super(WARNINGS_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) throws ParsingException {
        builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"))
                .setType(matcher.group("type"))
                .setMessage(appendPeriod(matcher))
                .guessSeverity(matcher.group("severity"))
                .setDescription(createDescription(lookahead));

        return builder.buildOptional();
    }
}
