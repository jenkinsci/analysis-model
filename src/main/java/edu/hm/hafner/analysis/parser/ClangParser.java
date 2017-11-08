package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the Clang compiler warnings.
 *
 * @author Neil Davis
 */
public class ClangParser extends RegexpLineParser {
    private static final long serialVersionUID = -3015592762345283182L;
    private static final String CLANG_WARNING_PATTERN = "^\\s*(?:\\d+%)?([^%]*?):(\\d+):(?:(\\d+):)?" + "(?:" +
            "(?:\\{\\d+:\\d+-\\d+:\\d+\\})+:)?\\s*(warning|[^\\[\\]]*error):" + "\\s*(.*?)(?:\\[([^\\[]*)\\])?$";

    /**
     * Creates a new instance of {@link ClangParser}.
     */
    public ClangParser() {
        super("clang", CLANG_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(5);
        if (message.matches("^-\\[.*\\].*$")) {
            return FALSE_POSITIVE;
        }

        String filename = matcher.group(1);
        int lineNumber = parseInt(matcher.group(2));
        int column = parseInt(matcher.group(3));
        String type = matcher.group(4);
        String category = matcher.group(6);

        Priority priority;
        if (type.contains("error")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }
        Issue warning = builder
                .setFileName(filename)
                .setLineStart(lineNumber)
                .setColumnStart(column)
                .setCategory(category)
                .setMessage(message)
                .setPriority(priority).build();
        return warning;
    }
}
