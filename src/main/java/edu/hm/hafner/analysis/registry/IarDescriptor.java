package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.IarParser;

/**
 * A Descriptor for the Iar parser.
 *
 * @author Lorenz Munsch
 */
class IarDescriptor extends ParserDescriptor {
    private static final String ID = "iar";
    private static final String NAME = "IAR Compiler (C/C++)";

    IarDescriptor() {
        super(ID, NAME, new IarParser());
    }
}
