package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.GhsMultiParser;

/**
 * A Descriptor for the GHS Multi parser.
 *
 * @author Lorenz Munsch
 */
class GhsMultiDescriptor extends ParserDescriptor {
    private static final String ID = "ghs-multi";
    private static final String NAME = "GHS Multi Compiler";

    GhsMultiDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new GhsMultiParser();
    }
}
