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
 * A parser for the IAR C/C++ compiler warnings. Note, that since release 4.1 this parser requires that IAR compilers
 * are started with option '----no_wrap_diagnostics'. Then the IAR compilers will create single-line warnings.
 *
 * @author Claus Klein
 * @author Ullrich Hafner
 * @author Jon Ware
 */
public class IarParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 7695540852439013425L;

    static final String IAR_WARNING_PATTERN = ANT_TASK
            + "(?:\"?(.*?)\"?[\\(,](\\d+)\\)?\\s+(?::\\s)?)?(Error|Remark|Warning|Fatal [Ee]rror)\\[(\\w+)\\]: (.*)$";

    /**
     * Creates a new instance of {@link IarParser}.
     */
    public IarParser() {
        super(IAR_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning") || line.contains("rror") || line.contains("Remark");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setSeverity(mapPriority(matcher))
                .setMessage(normalizeWhitespaceInMessage(matcher.group(5)))
                .setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(4))
                .buildOptional();
    }

    private Severity mapPriority(final Matcher matcher) {
        Severity priority;
        if (equalsIgnoreCase(matcher.group(3), "Remark")) {
            priority = Severity.WARNING_LOW;
        }
        else if (equalsIgnoreCase(matcher.group(3), "Error")) {
            priority = Severity.ERROR;
        }
        else if (equalsIgnoreCase(matcher.group(3), "Fatal error")) {
            priority = Severity.ERROR;
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
