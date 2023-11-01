package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SonarQubeDiffParser;
import edu.hm.hafner.analysis.parser.SonarQubeIssuesParser;

/**
 * A descriptor for the Sonar Qube issues parser.
 *
 * @author Lorenz Munsch
 */
public class SonarQubeDescriptor extends CompositeParserDescriptor {
    private static final String ID = "sonar";
    private static final String NAME = "SonarQube Issues";

    public SonarQubeDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new SonarQubeIssuesParser(), new SonarQubeDiffParser());
    }

    @Override
    public String getPattern() {
        return "**/sonar-report.json";
    }
}
