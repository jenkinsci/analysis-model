package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.JavaDocParser;

/**
 * A Descriptor for the Java Doc parser.
 *
 * @author Lorenz Munsch
 */
class JavaDocDescriptor extends ParserDescriptor {

    private static final String ID = "java-doc";
    private static final String NAME = "JavaDoc";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    JavaDocDescriptor() {
        super(ID, NAME, new JavaDocParser());
    }
}
