package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.JUnitParser;

public class JUnitAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -1595503635554896281L;

    @Override
    protected JUnitParser createParser() {
        return new JUnitParser();
    }
}
