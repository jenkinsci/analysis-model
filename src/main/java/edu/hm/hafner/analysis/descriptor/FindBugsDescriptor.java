package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.ErrorProneParser;
import edu.hm.hafner.analysis.parser.FindBugsParser;
import edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty;

/**
 * A Descriptor for the Find Bugs parser.
 *
 * @author Lorenz Munsch
 */
public class FindBugsDescriptor extends ParserDescriptor {

    private static final String ID = "find_bugs";
    private static final String NAME = "FindBugs";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public FindBugsDescriptor() {
        super(ID, NAME, new FindBugsParser(PriorityProperty.RANK));
    }
}
