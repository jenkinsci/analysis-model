package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.JavacParser;

/**
 * A descriptor for the javac compiler.
 *
 * @author Lorenz Munsch
 */
class JavacDescriptor extends ParserDescriptor {
    private static final String ID = "java";
    private static final String NAME = "Java Compiler";

    JavacDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new JavacParser();
    }
}
