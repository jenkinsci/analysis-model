package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ErrorProneParser;

/**
 * A descriptor for Error Prone.
 *
 * @author Lorenz Munsch
 */
class ErrorProneDescriptor extends ParserDescriptor {
    private static final String ID = "error-prone";
    private static final String NAME = "ErrorProne";

    ErrorProneDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new ErrorProneParser();
    }

    @Override
    public String getUrl() {
        return "https://errorprone.info";
    }
}
