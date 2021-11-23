package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CodeCheckerParser;

/**
 * A descriptor as alias for the Codechecker parser.
 * CodeChecker can run clang-tidy and clang static analysis in one run and
 * put all warnings in the same file.
 * Or the user can choose to run either clang-tidy or clang static analysis.
 * Both are different analysis types with different scopes and
 * likelihood of false positives.
 * They should be displayed as different results on jenkins.
 */
class CodeCheckerClangTidyDescriptor extends ParserDescriptor {
    private static final String ID = "code-checker-clang-tidy";
    private static final String NAME = "CodeCheckerClangTidy";

    CodeCheckerClangTidyDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CodeCheckerParser();
    }
}
