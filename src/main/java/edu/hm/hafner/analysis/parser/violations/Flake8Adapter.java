package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.Flake8Parser;

/**
 * Parses Flake8 files.
 *
 * @author Ullrich Hafner
 */
public class Flake8Adapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 4524731070497002381L;

    /**
     * Creates a new instance of {@link Flake8Adapter}.
     */
    public Flake8Adapter() {
        super(Rule.TYPE);
    }

    @Override
    protected Flake8Parser createParser() {
        return new Flake8Parser();
    }
}
