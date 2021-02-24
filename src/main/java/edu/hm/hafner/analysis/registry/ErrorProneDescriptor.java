package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.ErrorProneParser;

/**
 * A Descriptor for the Error Prone parser.
 *
 * @author Lorenz Munsch
 */
class ErrorProneDescriptor extends ParserDescriptor {

    private static final String ID = "error-prone";
    private static final String NAME = "ErrorProne";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    ErrorProneDescriptor() {
        super(ID, NAME, new ErrorProneParser());
    }
}
