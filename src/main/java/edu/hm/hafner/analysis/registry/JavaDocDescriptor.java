package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.JavaDocParser;

/**
 * A Descriptor for the Java Doc parser.
 *
 * @author Lorenz Munsch
 */
class JavaDocDescriptor extends ParserDescriptor {
    private static final String ID = "java-doc";
    private static final String NAME = "JavaDoc";

    JavaDocDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new JavaDocParser();
    }
}
