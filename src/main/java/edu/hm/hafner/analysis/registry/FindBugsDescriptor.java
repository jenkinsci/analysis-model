package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.FindBugsParser;
import edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty;

/**
 * A descriptor for the Find Bugs parser.
 *
 * @author Lorenz Munsch
 */
class FindBugsDescriptor extends ParserDescriptor {
    private static final String ID = "findbugs";
    private static final String NAME = "FindBugs";

    FindBugsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new FindBugsParser(PriorityProperty.RANK);
    }

    @Override
    public String getPattern() {
        return "**/findbugsXml.xml";
    }
}
