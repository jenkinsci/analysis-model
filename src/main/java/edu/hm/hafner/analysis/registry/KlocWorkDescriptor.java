package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.KlocWorkAdapter;

/**
 * A descriptor for Klocwork.
 *
 * @author Lorenz Munsch
 */
class KlocWorkDescriptor extends ParserDescriptor {
    private static final String ID = "klocwork";
    private static final String NAME = "Klocwork";

    KlocWorkDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new KlocWorkAdapter();
    }
}
