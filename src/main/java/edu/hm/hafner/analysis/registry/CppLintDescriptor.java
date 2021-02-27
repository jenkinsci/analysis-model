package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.CppLintParser;

/**
 * A Descriptor for the Cpp Lint parser.
 *
 * @author Lorenz Munsch
 */
class CppLintDescriptor extends ParserDescriptor {
    private static final String ID = "cpplint";
    private static final String NAME = "CppLint";

    CppLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new CppLintParser();
    }
}
