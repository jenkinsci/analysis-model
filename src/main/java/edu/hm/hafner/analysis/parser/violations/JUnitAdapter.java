package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.JUnitParser;

/**
 * Parses JUnit files.
 *
 * @author Gyanesha Prajjwal
 */
public class JUnitAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -1595503635554896281L;

    @Override
    JUnitParser createParser() {
        return new JUnitParser();
    }
}
