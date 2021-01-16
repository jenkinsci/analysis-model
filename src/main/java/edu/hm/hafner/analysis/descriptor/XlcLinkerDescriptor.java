package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.XlcCompilerParser;
import edu.hm.hafner.analysis.parser.XlcLinkerParser;

/**
 * A Descriptor for the Xlc Linker parser.
 *
 * @author Lorenz Munsch
 */
public class XlcLinkerDescriptor extends ParserDescriptor {

    private static final String ID = "xlc_linker";
    private static final String NAME = "XlcLinker";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public XlcLinkerDescriptor() {
        super(ID, NAME, new XlcLinkerParser());
    }
}
