package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.SonarQubeIssuesParser;

/**
 * A Descriptor for the Sonar Qube Issue parser.
 *
 * @author Lorenz Munsch
 */
class SonarQubeIssueDescriptor extends ParserDescriptor {

    private static final String ID = "sonar_Issue";
    private static final String NAME = "SonarQubeIssue";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    SonarQubeIssueDescriptor() {
        super(ID, NAME, new SonarQubeIssuesParser());
    }
}
