package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.LintParser;

/**
 * A descriptor for CSS-Lint.
 *
 * @author Lorenz Munsch
 */
class CssLintDescriptor extends ParserDescriptor {
    private static final String ID = "csslint";
    private static final String NAME = "CSS-Lint";

    CssLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new LintParser();
    }
}
