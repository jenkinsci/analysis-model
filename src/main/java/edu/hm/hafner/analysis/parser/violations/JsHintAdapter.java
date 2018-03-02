package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.JSHintParser;

/**
 * Parses JSHint files.
 *
 * @author Ullrich Hafner
 */
public class JsHintAdapter extends AbstractViolationAdapter {
    /**
     * Creates a new instance of {@link JsHintAdapter}.
     */
    public JsHintAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected JSHintParser createParser() {
        return new JSHintParser();
    }
}
