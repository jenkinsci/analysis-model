package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.LintParser;

/**
 * A descriptor for JSLint.
 *
 * @author Lorenz Munsch
 */
class JsLintDescriptor extends ParserDescriptor {
    private static final String ID = "jslint";
    private static final String NAME = "JSLint";

    JsLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new LintParser();
    }
}
