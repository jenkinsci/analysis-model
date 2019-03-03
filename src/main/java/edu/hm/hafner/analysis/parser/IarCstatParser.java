package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

import java.util.Optional;
import java.util.regex.Matcher;

/**
 * A parser for the IAR CSTAT warnings.
 *
 * @author Lorenz Aebi
 */
public class IarCstatParser extends RegexpLineParser {
    private static final long serialVersionUID = 7695540852439013425L;

    private static final String IAR_WARNING_PATTERN = ANT_TASK
            + "(?:\"?(.*?)\"?[\\(,](\\d+)\\)?\\s+)?(Severity-(?:Low|Medium|High))\\[(\\S+)\\]:(.*)$";

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
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setSeverity(mapSeverity(matcher.group(3)))
                .setMessage(normalizeWhitespaceInMessage(matcher.group(5)))
                .setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(4))
                .buildOptional();
    }

    private Severity mapSeverity(final String category) {
        Severity severity;
        if ("Severity-Low".equals(category)) {
            severity = Severity.WARNING_LOW;
        }
        else if ("Severity-High".equals(category)) {
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
