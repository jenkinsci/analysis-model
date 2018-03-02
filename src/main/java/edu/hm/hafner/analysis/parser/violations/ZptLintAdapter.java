package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.ZPTLintParser;

/**
 * Parses ZPTLint results files.
 *
 * @author Ullrich Hafner
 */
public class ZptLintAdapter extends AbstractViolationAdapter {
    /**
     * Creates a new instance of {@link ZptLintAdapter}.
     */
    public ZptLintAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected ZPTLintParser createParser() {
        return new ZPTLintParser();
    }
}
