package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.LintParser;

/**
 * A Descriptor for the Lint parser.
 *
 * @author Lorenz Munsch
 */
class LintDescriptor extends ParserDescriptor {

    private static final String ID = "lint";
    private static final String NAME = "Lint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    LintDescriptor() {
        super(ID, NAME, new LintParser());
    }
}
