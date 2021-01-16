package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.NagFortranParser;
import edu.hm.hafner.analysis.parser.P4Parser;

/**
 * A Descriptor for the P4 parser.
 *
 * @author Lorenz Munsch
 */
public class P4Descriptor extends ParserDescriptor {

    private static final String ID = "p4";
    private static final String NAME = "P4";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public P4Descriptor() {
        super(ID, NAME, new P4Parser());
    }
}
