package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CodeCheckerParser;

/**
 * A descriptor for the CodeChecker parser.
 *
 */
class CodeCheckerDescriptor extends ParserDescriptor {
    private static final String ID = "code-checker";
    private static final String NAME = "CodeChecker";

    CodeCheckerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CodeCheckerParser();
    }
}
