package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GradleErrorProneParser;

/**
 * A descriptor for the Gradle Error Prone parser.
 *
 * @author Lorenz Munsch
 */
class GradleErrorProneDescriptor extends ParserDescriptor {
    private static final String ID = "gradle-error-prone";
    private static final String NAME = "GradleErrorProne";

    GradleErrorProneDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new GradleErrorProneParser();
    }
}
