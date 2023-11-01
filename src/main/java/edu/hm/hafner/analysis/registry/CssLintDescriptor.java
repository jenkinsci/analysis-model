package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.LintParser;

/**
 * A descriptor for CSS-Lint.
 *
 * @author Lorenz Munsch
 */
public class CssLintDescriptor extends ParserDescriptor {
    private static final String ID = "csslint";
    private static final String NAME = "CSS-Lint";

    public CssLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new LintParser();
    }
}
