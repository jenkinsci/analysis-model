package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.ResharperParser;
import se.bjurr.violations.lib.parsers.ViolationsParser;

/**
 * A parser for the Resharper InspectCode compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class ResharperInspectCodeAdapter extends AbstractViolationAdapter {
    /**
     * Creates a new instance of {@link ResharperInspectCodeAdapter}.
     */
    public ResharperInspectCodeAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected ViolationsParser createParser() {
        return new ResharperParser();
    }
}
