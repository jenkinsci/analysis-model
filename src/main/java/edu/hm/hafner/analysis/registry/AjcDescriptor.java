package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.AjcParser;

/**
 * A descriptor for the AspectJ (ajc) Compiler.
 *
 * @author Lorenz Munsch
 */
class AjcDescriptor extends ParserDescriptor {
    private static final String ID = "aspectj";
    private static final String NAME = "AspectJ";

    AjcDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new AjcParser();
    }
}
