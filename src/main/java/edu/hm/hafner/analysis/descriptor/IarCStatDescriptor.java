package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.ErrorProneParser;
import edu.hm.hafner.analysis.parser.IarCstatParser;

/**
 * A Descriptor for the Iar C Stat parser.
 *
 * @author Lorenz Munsch
 */
public class IarCStatDescriptor extends ParserDescriptor {

    private static final String ID = "iar-cstat";
    private static final String NAME = "IAR C-STAT";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public IarCStatDescriptor() {
        super(ID, NAME, new IarCstatParser());
    }
}
