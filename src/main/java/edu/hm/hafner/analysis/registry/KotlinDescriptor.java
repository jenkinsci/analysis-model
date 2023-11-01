package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.JavacParser;

/**
 * A descriptor for Kotlin errors and warnings.
 *
 * @author Lorenz Munsch
 */
public class KotlinDescriptor extends ParserDescriptor {
    private static final String ID = "kotlin";
    private static final String NAME = "Kotlin";

    public KotlinDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new JavacParser();
    }
}
