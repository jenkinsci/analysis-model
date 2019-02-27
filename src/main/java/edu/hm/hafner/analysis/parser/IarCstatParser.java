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
            + "(?:\"?(.*?)\"?[\\(,](\\d+)\\)?\\s+(:\\s)?)?(Severity-Low|Severity-Medium|Severity-High)\\[(\\S+)\\]:\\s?(.*)$";

    /**
     * Creates a new instance of {@link IarCstatParser}.
     */
    public IarCstatParser() {
        super(IAR_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Severity-Low") || line.contains("Severity-Medium") || line.contains("Severity-High");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setSeverity(mapPriority(matcher))
                .setMessage(normalizeWhitespaceInMessage(matcher.group(6)))
                .setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(5))
                .buildOptional();
    }

    private Severity mapPriority(final Matcher matcher) {
        Severity priority;
        if ("Severity-Low".equalsIgnoreCase(matcher.group(4))) {
            priority = Severity.WARNING_LOW;
        }
        else if ("Severity-High".equalsIgnoreCase(matcher.group(4))) {
            priority = Severity.WARNING_HIGH;
        }
        else {
            priority = Severity.WARNING_NORMAL;
        }
        return priority;
    }

    private String normalizeWhitespaceInMessage(final String message) {
        return message.replaceAll("\\s+", " ");
    }
}
