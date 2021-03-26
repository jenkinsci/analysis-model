package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.AntJavacParser;
import edu.hm.hafner.analysis.parser.JavacParser;

/**
 * A descriptor for the javac compiler.
 *
 * @author Lorenz Munsch
 */
class JavaDescriptor extends CompositeParserDescriptor {
    private static final String ID = "java";
    private static final String NAME = "Java Compiler";

    JavaDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new JavacParser(), new AntJavacParser());
    }
}
