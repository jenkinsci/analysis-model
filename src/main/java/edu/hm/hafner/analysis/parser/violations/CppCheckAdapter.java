package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.CPPCheckParser;

/**
 * Parses CPPCheck files.
 *
 * @author Ullrich Hafner
 */
public class CppCheckAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 2244442395053328008L;

    @Override
    protected CPPCheckParser createParser() {
        return new CPPCheckParser();
    }
}
