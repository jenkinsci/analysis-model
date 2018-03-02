package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.KlocworkParser;

/**
 * Parses Klocwork files.
 *
 * @author Ullrich Hafner
 */
public class KlocWorkAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 5676554459268768313L;

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
