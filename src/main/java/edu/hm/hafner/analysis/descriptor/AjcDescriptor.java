package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.AjcParser;

/**
 * A Descriptor for the Ajc warnings.
 *
 * @author Lorenz Munsch
 */
class AjcDescriptor extends ParserDescriptor {

    private static final String ID = "aspectj";
    private static final String NAME = "AspectJ";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    AjcDescriptor() {
        super(ID, NAME, new AjcParser());
    }
}
