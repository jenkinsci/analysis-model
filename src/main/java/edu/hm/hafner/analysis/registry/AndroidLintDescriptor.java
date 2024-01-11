package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.AndroidLintAdapter;

/**
 * A descriptor for Android Lint.
 *
 * @author Lorenz Munsch
 */
class AndroidLintDescriptor extends ParserDescriptor {
    private static final String ID = "android-lint";
    private static final String NAME = "Android Lint";

    AndroidLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new AndroidLintAdapter();
    }

    @Override
    public String getHelp() {
        return "Use the flag -p.";
    }
}
