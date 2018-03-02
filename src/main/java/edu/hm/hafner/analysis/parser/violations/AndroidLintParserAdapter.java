package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.AndroidLintParser;

/**
 * Parses Android Lint files.
 *
 * @author Ullrich Hafner
 */
public class AndroidLintParserAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 2441144477814669681L;

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
