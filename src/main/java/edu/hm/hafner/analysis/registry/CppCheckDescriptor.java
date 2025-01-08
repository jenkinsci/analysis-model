package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.CppCheckAdapter;

/**
 * A descriptor for CPPCheck.
 *
 * @author Lorenz Munsch
 */
class CppCheckDescriptor extends ParserDescriptor {
    private static final String ID = "cppcheck";
    private static final String NAME = "CPPCheck";

    CppCheckDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CppCheckAdapter();
    }

    @Override
    public String getHelp() {
        return "Use options --xml --xml-version=2";
    }
}
