package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.CppCheckAdapter;

/**
 * A descriptor for CPPCheck.
 *
 * @author Lorenz Munsch
 */
public class CppCheckDescriptor extends ParserDescriptor {
    private static final String ID = "cppcheck";
    private static final String NAME = "CPPCheck";

    public CppCheckDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CppCheckAdapter();
    }

    @Override
    public String getHelp() {
        return "Use options --xml --xml-version=2";
    }
}
