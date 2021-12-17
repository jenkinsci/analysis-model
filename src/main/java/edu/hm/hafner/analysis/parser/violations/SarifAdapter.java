package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.SarifParser;

/**
 * Parses SARIF files.
 *
 * @author Ullrich Hafner
 */
public class SarifAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -5699747899173867285L;

    @Override
    SarifParser createParser() {
        return new SarifParser();
    }
}
