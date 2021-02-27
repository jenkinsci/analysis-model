package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.MetrowerksCwLinkerParser;

/**
 * A Descriptor for the Metrowerks Cw Linker parser.
 *
 * @author Lorenz Munsch
 */
class MetrowerksCwLinkerDescriptor extends ParserDescriptor {
    private static final String ID = "metrowerks-cw-linker";
    private static final String NAME = "MetrowerksCwLinker";

    MetrowerksCwLinkerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new MetrowerksCwLinkerParser();
    }
}
