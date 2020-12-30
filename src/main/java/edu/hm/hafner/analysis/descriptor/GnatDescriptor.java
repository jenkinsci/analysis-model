package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.GnatParser;

/**
 * A Descriptor for the Gnat parser.
 *
 * @author Lorenz Munsch
 */
class GnatDescriptor extends ParserDescriptor {

    private static final String ID = "gnat";
    private static final String NAME = "Ada Compiler (gnat)";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    GnatDescriptor() {
        super(ID, NAME, new GnatParser());
    }
}
