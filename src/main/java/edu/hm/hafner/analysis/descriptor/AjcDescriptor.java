package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.AjcParser;

/**
 * A Descriptor for the Ajc warnings.
 *
 * @author Lorenz Munsch
 */
public class AjcDescriptor extends ParserDescriptor {

    private static final String ID = "ajc";
    private static final String NAME = "Ajc";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public AjcDescriptor() {
        super(ID, NAME, new AjcParser());
    }
}
