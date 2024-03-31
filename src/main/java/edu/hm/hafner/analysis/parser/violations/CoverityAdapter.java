package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.CoverityParser;

/**
 * Parses Coverity JSON V7 report files.
 *
 * @author Jobin Jose
 */
public class CoverityAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -8210423965588732109L;

    @Override
    CoverityParser createParser() {
        return new CoverityParser();
    }
}