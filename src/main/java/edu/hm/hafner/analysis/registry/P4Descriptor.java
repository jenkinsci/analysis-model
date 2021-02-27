package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.P4Parser;

/**
 * A Descriptor for the P4 parser.
 *
 * @author Lorenz Munsch
 */
class P4Descriptor extends ParserDescriptor {
    private static final String ID = "p4";
    private static final String NAME = "P4";

    P4Descriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new P4Parser();
    }
}
