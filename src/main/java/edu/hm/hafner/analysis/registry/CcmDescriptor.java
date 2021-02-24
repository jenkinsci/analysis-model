package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.ccm.CcmParser;

/**
 * A Descriptor for the Ccm warnings.
 *
 * @author Lorenz Munsch
 */
class CcmDescriptor extends ParserDescriptor {

    private static final String ID = "ccm";
    private static final String NAME = "CCM";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    CcmDescriptor() {
        super(ID, NAME, new CcmParser());
    }
}
