package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.Flake8Parser;

/**
 * Parses Flake8 files.
 *
 * @author Ullrich Hafner
 */
public class Flake8Adapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = 4524731070497002381L;

    @Override
    Flake8Parser createParser() {
        return new Flake8Parser();
    }
}
