package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.GoogleErrorProneParser;

/**
 * Parses ErrorProne files.
 *
 * @author Ullrich Hafner
 */
public class ErrorProneAdapter extends AbstractViolationAdapter {
    /**
     * Creates a new instance of {@link ErrorProneAdapter}.
     */
    public ErrorProneAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected GoogleErrorProneParser createParser() {
        return new GoogleErrorProneParser();
    }
}
