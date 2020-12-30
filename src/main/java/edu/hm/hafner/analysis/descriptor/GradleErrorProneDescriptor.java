package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.ErrorProneParser;

/**
 * A Descriptor for the Gradle Error Prone parser.
 *
 * @author Lorenz Munsch
 */
class GradleErrorProneDescriptor extends ParserDescriptor {

    private static final String ID = "gradle-error-prone";
    private static final String NAME = "GradleErrorProne";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    GradleErrorProneDescriptor() {
        super(ID, NAME, new ErrorProneParser());
    }
}
