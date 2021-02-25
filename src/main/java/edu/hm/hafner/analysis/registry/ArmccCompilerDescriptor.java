package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.ArmccCompilerParser;

/**
 * A Descriptor for the ArmccCompiler warnings.
 *
 * @author Lorenz Munsch
 */
class ArmccCompilerDescriptor extends ParserDescriptor {
    private static final String ID = "armcc";
    private static final String NAME = "Armcc Compiler";

    ArmccCompilerDescriptor() {
        super(ID, NAME, new ArmccCompilerParser());
    }
}
