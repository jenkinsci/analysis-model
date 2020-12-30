package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.PreFastParser;

/**
 * A Descriptor for the Pre Fast parser.
 *
 * @author Lorenz Munsch
 */
class PreFastDescriptor extends ParserDescriptor {

    private static final String ID = "prefast";
    private static final String NAME = "PREfast";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    PreFastDescriptor() {
        super(ID, NAME, new PreFastParser());
    }
}
