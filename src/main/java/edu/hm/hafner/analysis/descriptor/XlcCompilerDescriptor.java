package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.TnsdlParser;
import edu.hm.hafner.analysis.parser.XlcCompilerParser;

/**
 * A Descriptor for the Xlc Compiler parser.
 *
 * @author Lorenz Munsch
 */
public class XlcCompilerDescriptor extends ParserDescriptor {

    private static final String ID = "xlc-compiler";
    private static final String NAME = "IBM XLC Compiler";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public XlcCompilerDescriptor() {
        super(ID, NAME, new XlcCompilerParser());
    }
}
