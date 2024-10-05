package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ErrorProneParser;
import edu.hm.hafner.analysis.parser.GradleErrorProneParser;

/**
 * A descriptor for Error Prone.
 *
 * @author Lorenz Munsch
 */
class ErrorProneDescriptor extends CompositeParserDescriptor {
    private static final String ID = "error-prone";
    private static final String NAME = "Error Prone";

    ErrorProneDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new ErrorProneParser(), new GradleErrorProneParser());
    }

    @Override
    public String getUrl() {
        return "https://errorprone.info";
    }

    @Override
    public Type getType() {
        return Type.BUG;
    }
}
