package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SbtScalacParser;

/**
 * A descriptor for Scala (with sbt).
 *
 * @author Lorenz Munsch
 */
class SbtScalacDescriptor extends ParserDescriptor {
    private static final String ID = "sbt";
    private static final String NAME = "SBT Scala";

    SbtScalacDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new SbtScalacParser();
    }
}
