package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.AcuCobolParser;

/**
 * A Descriptor for the AcuCobol warnings.
 *
 * @author Lorenz Munsch
 */
class AcuCobolDescriptor extends ParserDescriptor {

    private static final String ID = "acu-cobol";
    private static final String NAME = "AcuCobol";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    AcuCobolDescriptor() {
        super(ID, NAME, new AcuCobolParser());
    }
}
