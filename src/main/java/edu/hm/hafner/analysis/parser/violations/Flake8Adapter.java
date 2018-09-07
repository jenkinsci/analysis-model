package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.Flake8Parser;

/**
 * Parses Flake8 files.
 *
 * @author Ullrich Hafner
 */
public class Flake8Adapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 4524731070497002381L;

    @Override
    protected Flake8Parser createParser() {
        return new Flake8Parser();
    }
}
