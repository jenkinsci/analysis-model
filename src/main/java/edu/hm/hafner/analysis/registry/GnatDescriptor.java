package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.GnatParser;

/**
 * A Descriptor for the Gnat parser.
 *
 * @author Lorenz Munsch
 */
class GnatDescriptor extends ParserDescriptor {
    private static final String ID = "gnat";
    private static final String NAME = "Ada Compiler (gnat)";

    GnatDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new GnatParser();
    }
}
