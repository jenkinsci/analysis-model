package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.PyDocStyleAdapter;

/**
 * A descriptor for tPyDocStyle.
 *
 * @author Lorenz Munsch
 */
public class PyDocStyleDescriptor extends ParserDescriptor {
    private static final String ID = "pydocstyle";
    private static final String NAME = "PyDocStyle";

    public PyDocStyleDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new PyDocStyleAdapter();
    }
}
