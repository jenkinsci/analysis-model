package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.JavacParser;

/**
 * A Descriptor for the Javac parser.
 *
 * @author Lorenz Munsch
 */
class JavacDescriptor extends ParserDescriptor {

    private static final String ID = "javac";
    private static final String NAME = "Javac";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    JavacDescriptor() {
        super(ID, NAME, new JavacParser());
    }
}
