package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.IntelParser;

/**
 * A Descriptor for the Intel parser.
 *
 * @author Lorenz Munsch
 */
class IntelDescriptor extends ParserDescriptor {
    private static final String ID = "intel";
    private static final String NAME = "Intel Compiler (C, Fortran)";

    IntelDescriptor() {
        super(ID, NAME, new IntelParser());
    }
}
