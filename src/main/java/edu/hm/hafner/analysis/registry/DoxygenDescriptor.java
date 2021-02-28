package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.DoxygenParser;

/**
 * A descriptor for Doxygen.
 *
 * @author Lorenz Munsch
 */
class DoxygenDescriptor extends ParserDescriptor {
    private static final String ID = "doxygen";
    private static final String NAME = "Doxygen";

    DoxygenDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new DoxygenParser();
    }
}
