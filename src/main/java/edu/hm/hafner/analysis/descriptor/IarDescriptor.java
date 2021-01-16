package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.IarCstatParser;
import edu.hm.hafner.analysis.parser.IarParser;

/**
 * A Descriptor for the Iar parser.
 *
 * @author Lorenz Munsch
 */
public class IarDescriptor extends ParserDescriptor {

    private static final String ID = "iar";
    private static final String NAME = "Iar";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public IarDescriptor() {
        super(ID, NAME, new IarParser());
    }
}
