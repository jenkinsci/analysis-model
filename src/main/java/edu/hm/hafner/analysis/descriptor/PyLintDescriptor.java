package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.PuppetLintParser;
import edu.hm.hafner.analysis.parser.PyLintParser;

/**
 * A Descriptor for the Py Lint parser.
 *
 * @author Lorenz Munsch
 */
public class PyLintDescriptor extends ParserDescriptor {

    private static final String ID = "pylint";
    private static final String NAME = "Pylint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public PyLintDescriptor() {
        super(ID, NAME, new PyLintParser());
    }
}
