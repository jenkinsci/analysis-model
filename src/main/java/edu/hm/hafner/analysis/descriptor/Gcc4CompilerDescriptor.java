package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.FlexSdkParser;
import edu.hm.hafner.analysis.parser.Gcc4CompilerParser;

/**
 * A Descriptor for the Gcc 4 Compiler parser.
 *
 * @author Lorenz Munsch
 */
public class Gcc4CompilerDescriptor extends ParserDescriptor {

    private static final String ID = "gcc4";
    private static final String NAME = "GNU C Compiler (gcc)";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public Gcc4CompilerDescriptor() {
        super(ID, NAME, new Gcc4CompilerParser());
    }
}
