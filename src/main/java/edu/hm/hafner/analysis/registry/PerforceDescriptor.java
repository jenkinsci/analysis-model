package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.P4Parser;

/**
 * A descriptor for the Perforce tool.
 *
 * @author Lorenz Munsch
 */
class PerforceDescriptor extends ParserDescriptor {
    private static final String ID = "perforce";
    private static final String NAME = "Perforce Compiler";

    PerforceDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new P4Parser();
    }
}
