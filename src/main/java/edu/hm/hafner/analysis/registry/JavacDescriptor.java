package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.JavacParser;

/**
 * A Descriptor for the Javac parser.
 *
 * @author Lorenz Munsch
 */
class JavacDescriptor extends ParserDescriptor {
    private static final String ID = "javac";
    private static final String NAME = "Javac";

    JavacDescriptor() {
        super(ID, NAME, new JavacParser());
    }
}
