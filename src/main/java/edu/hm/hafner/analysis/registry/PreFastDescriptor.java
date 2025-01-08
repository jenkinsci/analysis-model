package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PreFastParser;

/**
 * A descriptor for Microsoft PreFast.
 *
 * @author Lorenz Munsch
 */
class PreFastDescriptor extends ParserDescriptor {
    private static final String ID = "prefast";
    private static final String NAME = "PREfast";

    PreFastDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new PreFastParser();
    }
}
