package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.DiabCParser;

/**
 * A Descriptor for the Diab C parser.
 *
 * @author Lorenz Munsch
 */
class DiabCDescriptor extends ParserDescriptor {

    private static final String ID = "diabc";
    private static final String NAME = "Wind River Diab Compiler (C/C++)";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    DiabCDescriptor() {
        super(ID, NAME, new DiabCParser());
    }
}
