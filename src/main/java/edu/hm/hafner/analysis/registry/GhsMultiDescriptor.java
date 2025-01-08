package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GhsMultiParser;

/**
 * A descriptor for the Ghs Multi Compiler.
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
    public IssueParser create(final Option... options) {
        return new GhsMultiParser();
    }
}
