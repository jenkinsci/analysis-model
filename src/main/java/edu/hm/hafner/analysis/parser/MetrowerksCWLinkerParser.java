package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for Metrowerks Codewarrior 4.x linker warnings.
 *
 * @author Sven Lübke
 */
public class MetrowerksCWLinkerParser extends RegexpLineParser {
    /** Pattern of MW CodeWarrior linker warnings. */
    private static final String CW_LINKER_WARNING_PATTERN = "^(INFORMATION|WARNING|ERROR) (.+)$";

    /**
     * Creates a new instance of <code>MetrowerksCWLinkerParser</code>.
     */
    public MetrowerksCWLinkerParser() {
        super("code-warrior-linker", CW_LINKER_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher, final IssueBuilder builder) {
        /* String fileName = matcher.group(3); */
        String message = matcher.group(2);
        String message_category = matcher.group(1);
        Priority priority;

        String category;
        if ("error".equalsIgnoreCase(message_category)) {
            priority = Priority.HIGH;
            category = "ERROR";
        }
        else if ("information".equalsIgnoreCase(message_category)) {
            priority = Priority.LOW;
            category = "Info";
        }
        else {
            priority = Priority.NORMAL;
            category = "Warning";
        }
        return builder.setFileName("See Warning message").setLineStart(0).setCategory(category)
                             .setMessage(message).setPriority(priority).build();
    }
}

