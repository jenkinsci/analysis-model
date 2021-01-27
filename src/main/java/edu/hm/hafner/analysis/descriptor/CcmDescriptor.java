package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.ccm.CcmParser;

/**
 * A Descriptor for the Ccm warnings.
 *
 * @author Lorenz Munsch
 */
public class CcmDescriptor extends ParserDescriptor {

    private static final String ID = "ccm";
    private static final String NAME = "CCM";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public CcmDescriptor() {
        super(ID, NAME, new CcmParser());
    }
}
