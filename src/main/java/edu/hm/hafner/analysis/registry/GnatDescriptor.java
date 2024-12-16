package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GnatParser;

/**
 * A descriptor for the Ada Compiler (gnat).
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
    public IssueParser create(final Option... options) {
        return new GnatParser();
    }
}
