package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ccm.CcmParser;

/**
 * A descriptor for the CCM.
 *
 * @author Lorenz Munsch
 */
class CcmDescriptor extends ParserDescriptor {
    private static final String ID = "ccm";
    private static final String NAME = "CCM";

    CcmDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CcmParser();
    }
}
