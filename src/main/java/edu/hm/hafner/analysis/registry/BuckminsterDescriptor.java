package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.BuckminsterParser;

/**
 * A descriptor for the Buckminster compiler.
 *
 * @author Lorenz Munsch
 */
class BuckminsterDescriptor extends ParserDescriptor {
    private static final String ID = "buckminster";
    private static final String NAME = "Buckminster";

    BuckminsterDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new BuckminsterParser();
    }
}
