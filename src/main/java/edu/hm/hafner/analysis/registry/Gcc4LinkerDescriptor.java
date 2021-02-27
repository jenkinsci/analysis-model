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

    Gcc4LinkerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new Gcc4LinkerParser();
    }
}
