package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.CodeNarcParser;

/**
 * Parses CodeNarc files.
 *
 * @author Ullrich Hafner
 */
public class CodeNarcAdapter extends AbstractViolationAdapter {
    /**
     * Creates a new instance of {@link CodeNarcAdapter}.
     */
    public CodeNarcAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected CodeNarcParser createParser() {
        return new CodeNarcParser();
    }
}
