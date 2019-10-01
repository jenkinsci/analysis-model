package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.parser.ErrorProneParser;

import se.bjurr.violations.lib.parsers.GoogleErrorProneParser;

/**
 * Parses ErrorProne files.
 *
 * @author Ullrich Hafner
 * @deprecated replaced by {@link ErrorProneParser}
 */
@Deprecated
public class ErrorProneAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -5800235784320729524L;

    @Override
    protected GoogleErrorProneParser createParser() {
        return new GoogleErrorProneParser();
    }
}
