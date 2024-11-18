package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.analysis.util.IntegerParser.*;

/**
 * A parser for armcc compiler warnings.
 *
 * @author Emanuele Zattin
 */
public class ArmccCompilerParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -2677728927938443703L;

    private static final String ARMCC_WARNING_PATTERN = "^\"(.+)\", line (\\d+): ([A-Z][a-z]+):\\D*(\\d+)\\D*?:\\s+(.+)$";

    /**
     * Creates a new instance of {@link ArmccCompilerParser}.
     */
    public ArmccCompilerParser() {
        super(ARMCC_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String type = matcher.group(3);
        int errorCode = parseInt(matcher.group(4));
        Severity priority;

        if (equalsIgnoreCase(type, "error")) {
            priority = Severity.WARNING_HIGH;
        }
        else {
            priority = Severity.WARNING_NORMAL;
        }
        String message = matcher.group(5);

        return builder.setFileName(matcher.group(1)).setLineStart(matcher.group(2)).setMessage(errorCode + " - " + message)
                      .setSeverity(priority).buildOptional();
    }
}
