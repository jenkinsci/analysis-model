package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.JavaDocParser;

/**
 * A descriptor for the Java Doc parser.
 *
 * @author Lorenz Munsch
 */
public class JavaDocDescriptor extends ParserDescriptor {
    private static final String ID = "javadoc-warnings";
    private static final String NAME = "JavaDoc";

    public JavaDocDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new JavaDocParser();
    }
}
