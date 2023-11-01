package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GoLintParser;

/**
 * A descriptor for Go Lint.
 *
 * @author Lorenz Munsch
 */
public class GoLintDescriptor extends ParserDescriptor {
    private static final String ID = "golint";
    private static final String NAME = "Go Lint";

    public GoLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new GoLintParser();
    }
}
