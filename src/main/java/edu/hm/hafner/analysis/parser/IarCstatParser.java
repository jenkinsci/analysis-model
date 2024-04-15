package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the IAR CSTAT warnings.
 *
 * @author Lorenz Aebi
 */
public class IarCstatParser extends LookaheadParser {
    private static final long serialVersionUID = 7695540852439013425L;

    private static final String IAR_WARNING_PATTERN = ANT_TASK
            + "(?:\"?(.*?)\"?[\\(,](\\d+)\\)?\\s+)?(Severity-(?:Low|Medium|High))\\[(\\S+)\\]:(.*)$";
    private static final String SEVERITY_LOW = "Severity-Low";
    private static final String SEVERITY_HIGH = "Severity-High";

    /**
     * Creates a new instance of {@link IarCstatParser}.
     */
    public IarCstatParser() {
        super(IAR_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Severity-");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setSeverity(mapSeverity(matcher.group(3)))
                .setMessage(normalizeWhitespaceInMessage(matcher.group(5)))
                .setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(4))
                .buildOptional();
    }

    private Severity mapSeverity(final String category) {
        Severity severity;
        if (SEVERITY_LOW.equals(category)) {
            severity = Severity.WARNING_LOW;
        }
        else if (SEVERITY_HIGH.equals(category)) {
            severity = Severity.WARNING_HIGH;
        }
        else {
            severity = Severity.WARNING_NORMAL;
        }
        return severity;
    }

    private String normalizeWhitespaceInMessage(final String message) {
        return message.replaceAll("\\s+", " ");
    }
}
