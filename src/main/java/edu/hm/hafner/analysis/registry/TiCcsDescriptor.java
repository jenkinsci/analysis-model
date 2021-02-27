package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.TiCcsParser;

/**
 * A Descriptor for the TiCcs parser.
 *
 * @author Lorenz Munsch
 */
class TiCcsDescriptor extends ParserDescriptor {
    private static final String ID = "code-composer";
    private static final String NAME = "Texas Instruments Code Composer Studio";

    TiCcsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new TiCcsParser();
    }
}
