package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.FindBugsParser;
import edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty;

/**
 * A descriptor for SpotBugs.
 *
 * @author Lorenz Munsch
 */
class SpotBugsDescriptor extends ParserDescriptor {
    private static final String ID = "spotbugs";
    private static final String NAME = "SpotBugs";

    SpotBugsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new FindBugsParser(PriorityProperty.RANK);
    }

    @Override
    public String getPattern() {
        return "**/spotbugsXml.xml";
    }

    @Override
    public String getUrl() {
        return "https://spotbugs.github.io";
    }
}
