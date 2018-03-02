package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.CPPCheckParser;

/**
 * Parses CPPCheck files.
 *
 * @author Ullrich Hafner
 */
public class CppCheckAdapter extends AbstractViolationAdapter {
    /**
     * Creates a new instance of {@link CppCheckAdapter}.
     */
    public CppCheckAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected CPPCheckParser createParser() {
        return new CPPCheckParser();
    }
}
