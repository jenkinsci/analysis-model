package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.Armcc5CompilerParser;

/**
 * A Descriptor for the Armcc5Compiler warnings.
 *
 * @author Lorenz Munsch
 */
public class Armcc5CompilerDescriptor extends ParserDescriptor {

    private static final String ID = "armcc_5_compiler";
    private static final String NAME = "Armcc5Compiler";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public Armcc5CompilerDescriptor() {
        super(ID, NAME,  new Armcc5CompilerParser());
    }

}
