package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.JavaDocParser;
import edu.hm.hafner.analysis.parser.JavacParser;

/**
 * A Descriptor for the Java Doc parser.
 *
 * @author Lorenz Munsch
 */
public class JavaDocDescriptor extends ParserDescriptor {

    private static final String ID = "java_doc";
    private static final String NAME = "JavaDoc";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public JavaDocDescriptor() {
        super(ID, NAME, new JavaDocParser());
    }
}
