package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.AcuCobolParser;

/**
 * A Descriptor for the AcuCobol warnings.
 *
 * @author Lorenz Munsch
 */
class AcuCobolDescriptor extends ParserDescriptor {
    private static final String ID = "acu-cobol";
    private static final String NAME = "AcuCobol";

    AcuCobolDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new AcuCobolParser();
    }
}
