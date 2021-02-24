package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.InvalidsParser;

/**
 * A Descriptor for the Invalids parser.
 *
 * @author Lorenz Munsch
 */
class InvalidsDescriptor extends ParserDescriptor {

    private static final String ID = "invalids";
    private static final String NAME = "Oracle Invalids";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    InvalidsDescriptor() {
        super(ID, NAME, new InvalidsParser());
    }
}
