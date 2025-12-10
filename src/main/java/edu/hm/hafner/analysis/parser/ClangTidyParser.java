package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * A parser for the clang-tidy static analysis warnings.
 *
 * @author Ryan Schaefer
 */
public class ClangTidyParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -3015592762345283182L;
    private static final String CLANG_TIDY_WARNING_PATTERN = "(?:clang-tidy\\S* (?:-\\S+ )*|)"
            + "((?<file>.+):(?<line>\\d+):(?<column>\\d+): |)(?<severity>warning|error): (?<message>.*?) \\[(?<category>[^\\s]*?)\\]$";
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
        // Reject GCC warnings that have [-W...]: Gcc4CompilerParser should handle these
        var category = matcher.group("category");
        if (category.startsWith("-W")) {
            return Optional.empty();
        }

        addDescriptionIfAvailable(builder, category);

        return builder.setFileName(matcher.group("file"))
                .setSeverity(Severity.guessFromString(matcher.group("severity")))
                .setLineStart(matcher.group("line"))
                .setColumnStart(matcher.group("column"))
                .setCategory(category)
                .setMessage(matcher.group("message"))
                .buildOptional();
    }

    // Clang-Tidy categories follow the pattern: <module>-<check>
    // Example: bugprone-forward-declaration-namespace
    // Documentation URL: https://clang.llvm.org/extra/clang-tidy/checks/<module>/<check>.html
    private void addDescriptionIfAvailable(final IssueBuilder builder, final String category) {
        if (!StringUtils.isBlank(category)
                && !category.startsWith("clang-diagnostic-")
                && !category.startsWith("clang-analyzer-")) {
            var parts = category.split("-", CATEGORY_PARTS_COUNT);
            if (parts.length == CATEGORY_PARTS_COUNT) {
                var module = parts[0];
                var check = parts[1];
                var url = String.format("https://clang.llvm.org/extra/clang-tidy/checks/%s/%s.html", module, check);

                builder.setDescription("See <a href=\"%s\">Clang-Tidy documentation</a>.".formatted(url));
            }
        }
    }
}
