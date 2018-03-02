package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.AndroidLintParser;

/**
 * Parses Android Lint files.
 *
 * @author Ullrich Hafner
 */
public class AndroidLintParserAdapter extends AbstractViolationAdapter {
    /**
     * Creates a new instance of {@link AndroidLintParserAdapter}.
     */
    public AndroidLintParserAdapter() {
        super(Rule.CATEGORY);
    }

    @Override
    protected AndroidLintParser createParser() {
        return new AndroidLintParser();
    }
}
