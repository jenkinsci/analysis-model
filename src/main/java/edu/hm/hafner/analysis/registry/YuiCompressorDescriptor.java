package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.YuiCompressorParser;

/**
 * A descriptor for the Yui Compressor parser.
 *
 * @author Lorenz Munsch
 */
class YuiCompressorDescriptor extends ParserDescriptor {
    private static final String ID = "yui";
    private static final String NAME = "YUI Compressor";

    YuiCompressorDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new YuiCompressorParser();
    }
}
