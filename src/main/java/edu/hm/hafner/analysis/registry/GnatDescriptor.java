package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GnatParser;

/**
 * A descriptor for the Ada Compiler (gnat).
 *
 * @author Lorenz Munsch
 */
public class GnatDescriptor extends ParserDescriptor {
    private static final String ID = "gnat";
    private static final String NAME = "Ada Compiler (gnat)";

    public GnatDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new GnatParser();
    }
}
