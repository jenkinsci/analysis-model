package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.Gcc4LinkerParser;

/**
 * A Descriptor for the Gcc 4 Compiler parser.
 *
 * @author Lorenz Munsch
 */
class Gcc4LinkerDescriptor extends ParserDescriptor {

    private static final String ID = "gcc-4-linker";
    private static final String NAME = "Gcc4Linker";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    Gcc4LinkerDescriptor() {
        super(ID, NAME, new Gcc4LinkerParser());
    }
}
