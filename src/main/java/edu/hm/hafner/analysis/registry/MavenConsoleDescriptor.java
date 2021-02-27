package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.MavenConsoleParser;

/**
 * A Descriptor for the Maven Console parser.
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
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new MavenConsoleParser();
    }
}
