package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.JsHintAdapter;

/**
 * A descriptor for JsHint.
 *
 * @author Lorenz Munsch
 */
class JsHintDescriptor extends ParserDescriptor {
    private static final String ID = "js-hint";
    private static final String NAME = "JsHint";

    JsHintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new JsHintAdapter();
    }
}
