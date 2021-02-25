package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.dry.dupfinder.DupFinderParser;

/**
 * A Descriptor for the Dup Finder parser.
 *
 * @author Lorenz Munsch
 */
class DupfinderDescriptor extends ParserDescriptor {
    private static final String ID = "dupfinder";
    private static final String NAME = "Resharper dupFinder";

    DupfinderDescriptor() {
        super(ID, NAME, new DupFinderParser());
    }
}
