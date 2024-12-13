package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.JavacParser;

/**
 * A descriptor for Kotlin errors and warnings.
 *
 * @author Lorenz Munsch
 */
class KotlinDescriptor extends ParserDescriptor {
    private static final String ID = "kotlin";
    private static final String NAME = "Kotlin";

    KotlinDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new JavacParser();
    }
}
