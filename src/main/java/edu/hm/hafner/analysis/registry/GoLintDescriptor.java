package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GoLintParser;

/**
 * A descriptor for Go Lint.
 *
 * @author Lorenz Munsch
 */
class GoLintDescriptor extends ParserDescriptor {
    private static final String ID = "golint";
    private static final String NAME = "Go Lint";

    GoLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new GoLintParser();
    }
}
