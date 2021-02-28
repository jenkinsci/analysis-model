package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SonarQubeDiffParser;

/**
 * A descriptor for the Sonar Qube Diff parser.
 *
 * @author Lorenz Munsch
 */
class SonarQubeDiffDescriptor extends ParserDescriptor {
    private static final String ID = "sonar-diff";
    private static final String NAME = "SonarQube Diff";

    SonarQubeDiffDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new SonarQubeDiffParser();
    }

    @Override
    public String getPattern() {
        return "**/sonar-report.json";
    }
}
