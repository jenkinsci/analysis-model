package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.JUnitAdapter;

/**
 * A descriptor for JUnit reports.
 *
 * @author Lorenz Munsch
 */
class JUnitDescriptor extends ParserDescriptor {
    private static final String ID = "junit";
    private static final String NAME = "JUnit";

    JUnitDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new JUnitAdapter();
    }

    @Override
    public String getUrl() {
        return "https://junit.org";
    }
}
