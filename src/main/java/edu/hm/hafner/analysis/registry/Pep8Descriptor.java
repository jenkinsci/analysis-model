package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.Pep8Parser;

/**
 * A descriptor for the PEP8 Python style guide.
 *
 * @author Lorenz Munsch
 */
class Pep8Descriptor extends ParserDescriptor {
    private static final String ID = "pep8";
    private static final String NAME = "PEP8";

    Pep8Descriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new Pep8Parser();
    }
}
