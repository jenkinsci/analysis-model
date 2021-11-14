package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CppLintParser;

/**
 * A descriptor for Cpplint.
 *
 * @author Lorenz Munsch
 */
class CppLintDescriptor extends ParserDescriptor {
    private static final String ID = "cpplint";
    private static final String NAME = "Cpplint";

    CppLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CppLintParser();
    }
}
