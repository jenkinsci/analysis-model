package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.PyLintParser;

/**
 * A Descriptor for the Py Lint parser.
 *
 * @author Lorenz Munsch
 */
class PyLintDescriptor extends ParserDescriptor {
    private static final String ID = "pylint";
    private static final String NAME = "Pylint";

    PyLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new PyLintParser();
    }
}
