package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.PiTestParser;

/**
 * Parses PIT results files.
 *
 * @author Ullrich Hafner
 */
public class PitAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -7811207963029906228L;

    /**
     * Creates a new instance of {@link PitAdapter}.
     */
    public PitAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected PiTestParser createParser() {
        return new PiTestParser();
    }
}
