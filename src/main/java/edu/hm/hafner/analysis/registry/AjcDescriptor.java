package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.AjcParser;

/**
 * A descriptor for the AspectJ (ajc) Compiler.
 *
 * @author Lorenz Munsch
 */
public class AjcDescriptor extends ParserDescriptor {
    private static final String ID = "aspectj";
    private static final String NAME = "AspectJ";

    public AjcDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new AjcParser();
    }
}
