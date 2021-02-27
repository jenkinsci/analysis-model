package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.SbtScalacParser;

/**
 * A Descriptor for the Sbt Scalac parser.
 *
 * @author Lorenz Munsch
 */
class SbtScalacDescriptor extends ParserDescriptor {
    private static final String ID = "sbt-scalac";
    private static final String NAME = "SbtScalac";

    SbtScalacDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new SbtScalacParser();
    }
}
