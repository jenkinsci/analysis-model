package edu.hm.hafner.analysis.registry;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ClangTidyParser;
import edu.umd.cs.findbugs.annotations.CheckForNull;

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
    public String getCategoryDocumentationUrl(@CheckForNull final String category) {
        if (StringUtils.isBlank(category)) {
            return StringUtils.EMPTY;
        }

        // Clang-Tidy categories follow the pattern: <module>-<check>
        // Example: bugprone-forward-declaration-namespace
        // Documentation URL: https://clang.llvm.org/extra/clang-tidy/checks/<module>/<check>.html

        if (category.startsWith("clang-diagnostic-")) {
            return StringUtils.EMPTY;
        }

        if (category.startsWith("clang-analyzer-")) {
            return StringUtils.EMPTY;
        }

        var parts = category.split("-", EXPECTED_PARTS_COUNT);
        if (parts.length == EXPECTED_PARTS_COUNT) {
            var module = parts[0];
            return String.format("https://clang.llvm.org/extra/clang-tidy/checks/%s/%s.html", module, category);
        }

        return StringUtils.EMPTY;
    }
}
