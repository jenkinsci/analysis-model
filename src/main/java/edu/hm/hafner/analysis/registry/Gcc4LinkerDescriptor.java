package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.Gcc4LinkerParser;

/**
 * A descriptor for the Gcc 4 Compiler parser.
 *
 * @author Lorenz Munsch
 */
class Gcc4LinkerDescriptor extends ParserDescriptor {
    private static final String ID = "gcc-linker";
    private static final String NAME = "GNU C Compiler (linker)";

    Gcc4LinkerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new Gcc4LinkerParser();
    }
}
