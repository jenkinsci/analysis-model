package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.dry.dupfinder.DupFinderParser;

/**
 * A descriptor for Resharper DupFinder.
 *
 * @author Lorenz Munsch
 */
public class DupfinderDescriptor extends DryDescriptor {
    private static final String ID = "dupfinder";
    private static final String NAME = "Resharper DupFinder";

    public DupfinderDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new DupFinderParser(getHighThreshold(options), getNormalThreshold(options));
    }
}
