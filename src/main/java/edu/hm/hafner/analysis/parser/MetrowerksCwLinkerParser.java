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
 * A parser for Metrowerks Codewarrior 4.x linker warnings.
 *
 * @author Sven Lübke
 */
public class MetrowerksCwLinkerParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 5993528761040876178L;

    /** Pattern of MW CodeWarrior linker warnings. */
    private static final String CW_LINKER_WARNING_PATTERN = "^(INFORMATION|WARNING|ERROR) (.+)$";

    /**
     * Creates a new instance of {@link MetrowerksCwLinkerParser}.
     */
    public MetrowerksCwLinkerParser() {
        super(CW_LINKER_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String message = matcher.group(2);
        String messageCategory = matcher.group(1);

        Severity priority;
        String category;
        if (equalsIgnoreCase(messageCategory, "error")) {
            priority = Severity.WARNING_HIGH;
            category = "ERROR";
        }
        else if (equalsIgnoreCase(messageCategory, "information")) {
            priority = Severity.WARNING_LOW;
            category = "Info";
        }
        else {
            priority = Severity.WARNING_NORMAL;
            category = "Warning";
        }
        return builder.setFileName("See Warning message")
                .setLineStart(0)
                .setCategory(category)
                .setMessage(message)
                .setSeverity(priority)
                .buildOptional();
    }
}
