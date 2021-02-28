package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PyLintParser;
import edu.hm.hafner.analysis.parser.violations.PyDocStyleAdapter;

/**
 * A descriptor for tPyDocStyle.
 *
 * @author Lorenz Munsch
 */
class PyDocDescriptor extends ParserDescriptor {
    private static final String ID = "pydocstyle";
    private static final String NAME = "PyDocStyle";

    PyDocDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new PyDocStyleAdapter();
    }
}
