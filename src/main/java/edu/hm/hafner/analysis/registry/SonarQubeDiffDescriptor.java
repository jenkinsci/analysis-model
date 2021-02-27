package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.SonarQubeDiffParser;

/**
 * A Descriptor for the Sonar Qube Diff parser.
 *
 * @author Lorenz Munsch
 */
class SonarQubeDiffDescriptor extends ParserDescriptor {
    private static final String ID = "sonar_diff";
    private static final String NAME = "SonarQubeDiff";

    SonarQubeDiffDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new SonarQubeDiffParser();
    }
}
