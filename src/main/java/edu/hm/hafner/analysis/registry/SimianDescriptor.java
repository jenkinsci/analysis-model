package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.dry.simian.SimianParser;

/**
 * A Descriptor for the Simian parser.
 *
 * @author Lorenz Munsch
 */
class SimianDescriptor extends ParserDescriptor {
    private static final String ID = "simian";
    private static final String NAME = "Simian";

    SimianDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new SimianParser();
    }
}
