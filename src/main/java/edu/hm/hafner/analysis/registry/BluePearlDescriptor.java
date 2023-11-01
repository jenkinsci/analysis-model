package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.BluePearlParser;

/**
 * A descriptor for the Blue Pearl Visual Verification Suite.
 *
 * @author Simon Matthews
 */
public class BluePearlDescriptor extends ParserDescriptor {
    private static final String ID = "bluepearl";
    private static final String NAME = "Blue Pearl Visual Verification Suite";

    public BluePearlDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new BluePearlParser();
    }
}
