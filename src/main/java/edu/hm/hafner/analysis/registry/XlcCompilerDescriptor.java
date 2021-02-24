package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.XlcCompilerParser;

/**
 * A Descriptor for the Xlc Compiler parser.
 *
 * @author Lorenz Munsch
 */
class XlcCompilerDescriptor extends ParserDescriptor {

    private static final String ID = "xlc-compiler";
    private static final String NAME = "IBM XLC Compiler";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    XlcCompilerDescriptor() {
        super(ID, NAME, new XlcCompilerParser());
    }
}
