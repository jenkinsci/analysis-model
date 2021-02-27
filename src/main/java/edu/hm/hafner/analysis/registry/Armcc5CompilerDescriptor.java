package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.Armcc5CompilerParser;

/**
 * A Descriptor for the Armcc5Compiler warnings.
 *
 * @author Lorenz Munsch
 */
class Armcc5CompilerDescriptor extends ParserDescriptor {
    private static final String ID = "armcc5";
    private static final String NAME = "Armcc5 Compiler";

    Armcc5CompilerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return   new Armcc5CompilerParser())
    }

}
