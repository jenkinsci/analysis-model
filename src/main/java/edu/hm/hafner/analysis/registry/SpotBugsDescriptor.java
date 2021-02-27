package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.FindBugsParser;
import edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty;

/**
 * A Descriptor for the SpotBugs parser.
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
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new FindBugsParser(PriorityProperty.RANK))
    }

    @Override
    public String getPattern() {
        return "**/spotbugsXml.xml";
    }
}
