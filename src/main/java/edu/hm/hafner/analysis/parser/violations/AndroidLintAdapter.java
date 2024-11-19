package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.AndroidLintParser;

/**
 * Parses Android Lint files.
 *
 * @author Ullrich Hafner
 */
public class AndroidLintAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = 2441144477814669681L;

    @Override
    AndroidLintParser createParser() {
        return new AndroidLintParser();
    }
}
