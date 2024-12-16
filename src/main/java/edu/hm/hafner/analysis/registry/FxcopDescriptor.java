package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.fxcop.FxCopParser;

/**
 * A descriptor for FxCop.
 *
 * @author Lorenz Munsch
 */
class FxcopDescriptor extends ParserDescriptor {
    private static final String ID = "fxcop";
    private static final String NAME = "FxCop";

    FxcopDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new FxCopParser();
    }
}
