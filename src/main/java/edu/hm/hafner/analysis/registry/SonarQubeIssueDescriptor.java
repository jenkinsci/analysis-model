package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SonarQubeIssuesParser;

/**
 * A descriptor for the Sonar Qube issues parser.
 *
 * @author Lorenz Munsch
 */
class SonarQubeIssueDescriptor extends ParserDescriptor {
    private static final String ID = "sonar";
    private static final String NAME = "SonarQube Issues";

    SonarQubeIssueDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new SonarQubeIssuesParser();
    }

    @Override
    public String getPattern() {
        return "**/sonar-report.json";
    }
}
