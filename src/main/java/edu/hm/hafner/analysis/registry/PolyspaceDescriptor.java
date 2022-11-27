package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PolyspaceParser;

/**
 * A descriptor for Polyspace tool.
 *
 * @author Eva Habeeb
 */

public class PolyspaceDescriptor extends ParserDescriptor {
    private static final String ID = "PolyspaceParse";
    private static final String NAME = "Polyspace Tool";

    PolyspaceDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new PolyspaceParser();
    }
}
