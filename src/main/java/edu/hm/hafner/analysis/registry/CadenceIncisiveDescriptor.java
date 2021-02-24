package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.CadenceIncisiveParser;

/**
 * A Descriptor for the CadenceIncisive warnings.
 *
 * @author Lorenz Munsch
 */
class CadenceIncisiveDescriptor extends ParserDescriptor {

    private static final String ID = "cadence";
    private static final String NAME = "Cadence Incisive";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    CadenceIncisiveDescriptor() {
        super(ID, NAME, new CadenceIncisiveParser());
    }
}
