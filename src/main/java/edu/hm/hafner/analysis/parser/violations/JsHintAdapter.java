package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.JSLintParser;

/**
 * Parses JSHint files.
 *
 * @author Ullrich Hafner
 */
public class JsHintAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = 2981825338893917845L;

    @Override
    JSLintParser createParser() {
        return new JSLintParser();
    }
}
