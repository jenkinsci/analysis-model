package edu.hm.hafner.analysis.parser;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = -3015592762345283182L;
    private static final String CLANG_TIDY_WARNING_PATTERN = "(?:clang-tidy\\S* (?:-\\S+ )*|)"
            + "((.+):(\\d+):(\\d+): |)(warning|error): (.*?) \\[([^\\s]*?)\\]$";
    private static final int CATEGORY_PARTS_COUNT = 2;

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

        // Reject GCC warnings that have [-W...] format
        // GCC warnings should be handled by Gcc4CompilerParser
        var category = matcher.group(7);
        if (category.startsWith("-W")) {
            return Optional.empty();
        }

        var message = matcher.group(6);
        var description = buildDescriptionWithDocumentation(message, category);

        return builder.setFileName(matcher.group(2))
                .setSeverity(priority)
                .setLineStart(matcher.group(3))
                .setColumnStart(matcher.group(4))
                .setType(StringUtils.capitalize(matcher.group(5)))
                .setCategory(category)
                .setMessage(message)
                .setDescription(description)
                .buildOptional();
    }

    private String buildDescriptionWithDocumentation(final String message, final String category) {
        // Clang-Tidy categories follow the pattern: <module>-<check>
        // Example: bugprone-forward-declaration-namespace
        // Documentation URL: https://clang.llvm.org/extra/clang-tidy/checks/<module>/<check>.html

        if (StringUtils.isBlank(category)) {
            return "";
        }

        if (category.startsWith("clang-diagnostic-") || category.startsWith("clang-analyzer-")) {
            return "";
        }

        var parts = category.split("-", CATEGORY_PARTS_COUNT);
        if (parts.length == CATEGORY_PARTS_COUNT) {
            var module = parts[0];
            var check = parts[1];
            var url = String.format("https://clang.llvm.org/extra/clang-tidy/checks/%s/%s.html", module, check);
            return String.format("%s See <a href=\"%s\">Clang-Tidy documentation</a>.", message, url);
        }

        return "";
    }
}
