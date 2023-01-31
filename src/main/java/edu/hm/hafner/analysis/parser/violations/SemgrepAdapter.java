package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.SemgrepParser;

/**
 * Parses PyDocStyle results files.
 *
 * @author Ullrich Hafner
 */
public class SemgrepAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 1119003057153007718L;

    @Override
    SemgrepParser createParser() {
        return new SemgrepParser();
    }
}
