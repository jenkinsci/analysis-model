package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.MavenConsoleParser;

/**
 * A descriptor for the Maven Console parser.
 *
 * @author Lorenz Munsch
 */
class MavenConsoleDescriptor extends ParserDescriptor {
    private static final String ID = "maven-warnings";
    private static final String NAME = "Maven";

    MavenConsoleDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new MavenConsoleParser();
    }

    @Override
    public String getUrl() {
        return "https://maven.apache.org/";
    }
}
