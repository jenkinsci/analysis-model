package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.GccParser;

/**
 * A Descriptor for the Gcc parser.
 *
 * @author Lorenz Munsch
 */
class GccDescriptor extends ParserDescriptor {

    private static final String ID = "gcc";
    private static final String NAME = "Gcc";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    GccDescriptor() {
        super(ID, NAME, new GccParser());
    }
}
