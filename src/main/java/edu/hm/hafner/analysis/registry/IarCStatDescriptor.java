package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.IarCstatParser;

/**
 * A Descriptor for the Iar C Stat parser.
 *
 * @author Lorenz Munsch
 */
class IarCStatDescriptor extends ParserDescriptor {
    private static final String ID = "iar-cstat";
    private static final String NAME = "IAR C-STAT";

    IarCStatDescriptor() {
        super(ID, NAME, new IarCstatParser());
    }
}
