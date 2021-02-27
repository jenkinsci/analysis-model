package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.Pep8Parser;

/**
 * A Descriptor for the Pep 8 parser.
 *
 * @author Lorenz Munsch
 */
class Pep8Descriptor extends ParserDescriptor {
    private static final String ID = "pep8";
    private static final String NAME = "Pep8";

    Pep8Descriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new Pep8Parser();
    }
}
