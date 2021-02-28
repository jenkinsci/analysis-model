package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.dry.simian.SimianParser;

/**
 * A descriptor for the Simian duplication scanner.
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
    public IssueParser createParser() {
        return new SimianParser();
    }
}
