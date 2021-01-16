package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.PerlCriticParser;
import edu.hm.hafner.analysis.parser.PhpParser;

/**
 * A Descriptor for the Php parser.
 *
 * @author Lorenz Munsch
 */
public class PhpDescriptor extends ParserDescriptor {

    private static final String ID = "php";
    private static final String NAME = "Php";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public PhpDescriptor() {
        super(ID, NAME, new PhpParser());
    }
}
