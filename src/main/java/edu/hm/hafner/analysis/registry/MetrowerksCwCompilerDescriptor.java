package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.MetrowerksCwCompilerParser;

/**
 * A Descriptor for the Metrowerks Cw Comnpiler parser.
 *
 * @author Lorenz Munsch
 */
class MetrowerksCwCompilerDescriptor extends ParserDescriptor {

    private static final String ID = "metrowerks";
    private static final String NAME = "Metrowerks CodeWarrior";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    MetrowerksCwCompilerDescriptor() {
        super(ID, NAME, new MetrowerksCwCompilerParser());
    }
}
