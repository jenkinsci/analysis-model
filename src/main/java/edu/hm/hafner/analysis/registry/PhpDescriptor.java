package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.PhpParser;

/**
 * A Descriptor for the Php parser.
 *
 * @author Lorenz Munsch
 */
class PhpDescriptor extends ParserDescriptor {

    private static final String ID = "php";
    private static final String NAME = "PHP Runtime";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    PhpDescriptor() {
        super(ID, NAME, new PhpParser());
    }
}
