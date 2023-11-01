package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.dry.simian.SimianParser;

/**
 * A descriptor for the Simian duplication scanner.
 *
 * @author Lorenz Munsch
 */
public class SimianDescriptor extends DryDescriptor {
    private static final String ID = "simian";
    private static final String NAME = "Simian";

    public SimianDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new SimianParser(getHighThreshold(options), getNormalThreshold(options));
    }
}
