package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the clang-tidy static analysis warnings.
 *
 * @author Ryan Schaefer
 */
public class ClangTidyParser extends LookaheadParser {
    private static final long serialVersionUID = -3015592762345283182L;
    private static final String CLANG_TIDY_WARNING_PATTERN = "(?:clang-tidy\\S* (?:-\\S+ )*|)"
            + "((.+):(\\d+):(\\d+): |)(warning|error): (.*?) \\[([^\\s]*?)\\]$";

    /**
     * Creates a new instance of {@link ClangTidyParser}.
     */
    public ClangTidyParser() {
        super(CLANG_TIDY_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        Severity priority;
        if (matcher.group(5).contains("error")) {
            priority = Severity.WARNING_HIGH;
        }
        else {
            priority = Severity.WARNING_NORMAL;
        }

        return builder.setFileName(matcher.group(2))
                .setSeverity(priority)
                .setLineStart(matcher.group(3))
                .setColumnStart(matcher.group(4))
                .setType(StringUtils.capitalize(matcher.group(5)))
                .setCategory(matcher.group(7))
                .setMessage(matcher.group(6))
                .buildOptional();
    }
}
