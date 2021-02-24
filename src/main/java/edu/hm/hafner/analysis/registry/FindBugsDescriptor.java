package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.FindBugsParser;
import edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty;

/**
 * A Descriptor for the Find Bugs parser.
 *
 * @author Lorenz Munsch
 */
class FindBugsDescriptor extends ParserDescriptor {

    private static final String ID = "findbugs";
    private static final String NAME = "FindBugs";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    FindBugsDescriptor() {
        super(ID, NAME, new FindBugsParser(PriorityProperty.RANK));
    }

    @Override
    public String getPattern() {
        return "**/findbugsXml.xml";
    }
}
