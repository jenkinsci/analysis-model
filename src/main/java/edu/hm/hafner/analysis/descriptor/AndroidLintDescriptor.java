package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.violations.AndroidLintParserAdapter;

/**
 * A Descriptor for the AndroidLint warnings.
 *
 * @author Lorenz Munsch
 */
public class AndroidLintDescriptor extends ParserDescriptor {

    private static final String ID = "android_lint";
    private static final String NAME = "AndroidLint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public AndroidLintDescriptor() {
        super(ID, NAME, new AndroidLintParserAdapter());
    }
}
