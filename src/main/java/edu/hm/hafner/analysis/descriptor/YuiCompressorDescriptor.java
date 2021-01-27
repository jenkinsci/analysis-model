package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.XlcLinkerParser;
import edu.hm.hafner.analysis.parser.YuiCompressorParser;

/**
 * A Descriptor for the Yui Compressor parser.
 *
 * @author Lorenz Munsch
 */
public class YuiCompressorDescriptor extends ParserDescriptor {

    private static final String ID = "yui";
    private static final String NAME = "YUI Compressor";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public YuiCompressorDescriptor() {
        super(ID, NAME, new YuiCompressorParser());
    }
}
