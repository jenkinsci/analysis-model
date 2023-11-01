package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.MavenConsoleParser;

/**
 * A descriptor for the Maven Console parser.
 *
 * @author Lorenz Munsch
 */
public class MavenConsoleDescriptor extends ParserDescriptor {
    private static final String ID = "maven-warnings";
    private static final String NAME = "Maven";

    public MavenConsoleDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new MavenConsoleParser();
    }
}
