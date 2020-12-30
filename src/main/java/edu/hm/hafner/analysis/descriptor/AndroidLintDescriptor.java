package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.violations.AndroidLintParserAdapter;

/**
 * A Descriptor for the AndroidLint warnings.
 *
 * @author Lorenz Munsch
 */
class AndroidLintDescriptor extends ParserDescriptor {

    private static final String ID = "android-lint";
    private static final String NAME = "Android Lint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    AndroidLintDescriptor() {
        super(ID, NAME, new AndroidLintParserAdapter());
    }
}
