package edu.hm.hafner.analysis.registry;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ClangTidyParser;

/**
 * A descriptor for the Clang-Tidy compiler.
 *
 * @author Lorenz Munsch
 */
class ClangTidyDescriptor extends ParserDescriptor {
    private static final String ID = "clang-tidy";
    private static final String NAME = "Clang-Tidy";
    private static final int EXPECTED_PARTS_COUNT = 2;

    ClangTidyDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new ClangTidyParser();
    }

    @Override
    public String getDescription(final Issue issue) {
        var description = issue.getDescription();
        var category = issue.getCategory();

        if (StringUtils.isBlank(category)) {
            return description;
        }

        // Clang-Tidy categories follow the pattern: <module>-<check>
        // Example: bugprone-forward-declaration-namespace
        // Documentation URL: https://clang.llvm.org/extra/clang-tidy/checks/<module>/<check>.html

        if (category.startsWith("clang-diagnostic-") || category.startsWith("clang-analyzer-")) {
            return description;
        }

        var parts = category.split("-", EXPECTED_PARTS_COUNT);
        if (parts.length == EXPECTED_PARTS_COUNT) {
            var module = parts[0];
            var check = parts[1];
            var url = String.format("https://clang.llvm.org/extra/clang-tidy/checks/%s/%s.html", module, check);
            return String.format("%s See <a href=\"%s\">Clang-Tidy documentation</a>.", description, url);
        }

        return description;
    }
}
