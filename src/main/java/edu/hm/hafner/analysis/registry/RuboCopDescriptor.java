package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.RuboCopParser;

/**
 * A descriptor for the RuboCop.
 *
 * @author Lorenz Munsch
 */
class RuboCopDescriptor extends ParserDescriptor {
    private static final String ID = "rubocop";
    private static final String NAME = "Rubocop";

    RuboCopDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new RuboCopParser();
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>rubocop --format progress</code>.";
    }
}
