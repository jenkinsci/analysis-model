package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.PyDocStyleParser;

/**
 * Parses PyDocStyle results files.
 *
 * @author Ullrich Hafner
 */
public class PyDocStyleAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 1119003057153007718L;

    @Override
    PyDocStyleParser createParser() {
        return new PyDocStyleParser();
    }
}
