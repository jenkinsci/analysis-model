package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.PyDocStyleAdapter;

/**
 * A descriptor for tPyDocStyle.
 *
 * @author Lorenz Munsch
 */
class PyDocStyleDescriptor extends ParserDescriptor {
    private static final String ID = "pydocstyle";
    private static final String NAME = "PyDocStyle";

    PyDocStyleDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new PyDocStyleAdapter();
    }
}
