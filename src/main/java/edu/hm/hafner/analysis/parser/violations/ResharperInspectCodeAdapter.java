package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.ResharperParser;
import se.bjurr.violations.lib.parsers.ViolationsParser;

/**
 * A parser for the Resharper InspectCode compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class ResharperInspectCodeAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -7285232072855215797L;

    @Override
    ViolationsParser createParser() {
        return new ResharperParser();
    }
}
