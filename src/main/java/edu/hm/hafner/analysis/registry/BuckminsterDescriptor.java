package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.BuckminsterParser;

/**
 * A Descriptor for the Buckminster warnings.
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
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new BuckminsterParser();
    }
}
