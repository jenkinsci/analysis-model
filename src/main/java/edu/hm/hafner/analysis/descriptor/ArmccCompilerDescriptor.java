package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.ArmccCompilerParser;

/**
 * A Descriptor for the ArmccCompiler warnings.
 *
 * @author Lorenz Munsch
 */
public class ArmccCompilerDescriptor extends ParserDescriptor {

    private static final String ID = "armcc_compiler";
    private static final String NAME = "ArmccCompiler";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public ArmccCompilerDescriptor() {
        super(ID, NAME, new ArmccCompilerParser());
    }
}
