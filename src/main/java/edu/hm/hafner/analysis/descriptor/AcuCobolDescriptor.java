package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.AcuCobolParser;

/**
 * A Descriptor for the AcuCobol warnings.
 *
 * @author Lorenz Munsch
 */
public class AcuCobolDescriptor extends ParserDescriptor {

    private static final String ID = "acu_cobol";
    private static final String NAME = "AcuCobol";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public AcuCobolDescriptor() {
        super(ID, NAME, new AcuCobolParser());
    }
}
