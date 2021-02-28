package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.MetrowerksCwLinkerParser;

/**
 * A descriptor for the Metrowerks CodeWarrior linker.
 *
 * @author Lorenz Munsch
 */
class MetrowerksCwLinkerDescriptor extends ParserDescriptor {
    private static final String ID = "metrowerks-linker";
    private static final String NAME = "Metrowerks CodeWarrior Linker";

    MetrowerksCwLinkerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new MetrowerksCwLinkerParser();
    }
}
