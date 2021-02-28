package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.EclipseMavenParser;

/**
 * A descriptor for the Eclipse compiler (in Maven).
 *
 * @author Lorenz Munsch
 */
class EclipseMavenDescriptor extends ParserDescriptor {
    private static final String ID = "eclipse-maven";
    private static final String NAME = "Eclipse Maven";

    EclipseMavenDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new EclipseMavenParser();
    }
}
