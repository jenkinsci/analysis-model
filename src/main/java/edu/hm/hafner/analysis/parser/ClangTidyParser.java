package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the clang-tidy static analysis warnings.
 *
 * @author Ryan Schaefer
 */
public class ClangTidyParser extends RegexpLineParser {
    private static final long serialVersionUID = -3015592762345283182L;
    private static final String CLANG_TIDY_WARNING_PATTERN =
            "([^\\s]+):(\\d+):(\\d+): (warning|error): (.*?) \\[([^\\s]*?)\\]$";

    /**
     * Creates a new instance of {@link ClangTidyParser}.
     */
    public ClangTidyParser() {
        super(CLANG_TIDY_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        Priority priority;
        if (matcher.group(4).contains("error")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }

        return builder.setFileName(matcher.group(1))
                .setPriority(priority)
                .setLineStart(parseInt(matcher.group(2)))
                .setColumnStart(parseInt(matcher.group(3)))
                .setType(StringUtils.capitalize(matcher.group(4)))
                .setCategory(matcher.group(6))
                .setMessage(matcher.group(5))
                .build();
    }
}
