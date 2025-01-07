package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.OeLintAdvParser;

/**
 * Descriptor for oelint-adv.
 */
class OeLintAdvDescriptor extends ParserDescriptor {
    private static final String ID = "oelint-adv";
    private static final String NAME = ID;

    OeLintAdvDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new OeLintAdvParser();
    }

    @Override
    public String getUrl() {
        return "https://github.com/priv-kweihmann/oelint-adv";
    }
}
