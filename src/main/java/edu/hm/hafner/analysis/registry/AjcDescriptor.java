package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.AjcParser;

/**
 * A Descriptor for the Ajc warnings.
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
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new AjcParser();
    }
}
