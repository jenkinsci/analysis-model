package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for C++ Lint compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class CppLintParser extends RegexpLineParser {
    private static final long serialVersionUID = 1737791073711198075L;

    private static final String PATTERN = "^\\s*(.*)\\s*[(:](\\d*)\\)?:\\s*(.*)\\s*\\[(.*)\\] \\[(.*)\\]$";

    /**
     * Creates a new instance of {@link CppLintParser}.
     */
    public CppLintParser() {
        super(PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        Priority priority = mapPriority(matcher.group(5));

        return builder.setFileName(matcher.group(1)).setLineStart(parseInt(matcher.group(2)))
                      .setCategory(matcher.group(4)).setMessage(matcher.group(3)).setPriority(priority).build();
    }

    private Priority mapPriority(final String priority) {
        int value = parseInt(priority);
        if (value >= 5) {
            return Priority.HIGH;
        }
        if (value >= 3) {
            return Priority.NORMAL;
        }
        return Priority.LOW;
    }
}

