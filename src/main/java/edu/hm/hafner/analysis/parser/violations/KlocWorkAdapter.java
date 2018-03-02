package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.KlocworkParser;

/**
 * Parses Klocwork files.
 *
 * @author Ullrich Hafner
 */
public class KlocWorkAdapter extends AbstractViolationAdapter {
    /**
     * Creates a new instance of {@link KlocWorkAdapter}.
     */
    public KlocWorkAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected KlocworkParser createParser() {
        return new KlocworkParser();
    }
}
