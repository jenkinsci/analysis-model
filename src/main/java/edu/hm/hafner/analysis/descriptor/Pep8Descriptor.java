package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.P4Parser;
import edu.hm.hafner.analysis.parser.Pep8Parser;

/**
 * A Descriptor for the Pep 8 parser.
 *
 * @author Lorenz Munsch
 */
public class Pep8Descriptor extends ParserDescriptor {

    private static final String ID = "pep8";
    private static final String NAME = "Pep8";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public Pep8Descriptor() {
        super(ID, NAME, new Pep8Parser());
    }
}
