package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.OELintAdvParser;

/**
 * Descriptor for oelint-adv.
 */
class OELintAdvDescriptor extends ParserDescriptor {
    private static final String ID = "oelint-adv";
    private static final String NAME = ID;

    OELintAdvDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new OELintAdvParser();
    }
}
