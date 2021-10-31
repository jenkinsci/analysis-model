package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CodeCheckerParser;

/**
 * A descriptor for the Clang-Tidy compiler.
 *
 */
class CodeCheckerDescriptor extends ParserDescriptor {
    private static final String ID = "codechecker";
    private static final String NAME = "CodeChecker";

    CodeCheckerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CodeCheckerParser();
    }
}
