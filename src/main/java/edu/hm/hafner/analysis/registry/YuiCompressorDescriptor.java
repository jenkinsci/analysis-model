package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.YuiCompressorParser;

/**
 * A descriptor for the Yui Compressor parser.
 *
 * @author Lorenz Munsch
 */
public class YuiCompressorDescriptor extends ParserDescriptor {
    private static final String ID = "yui";
    private static final String NAME = "YUI Compressor";

    public YuiCompressorDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new YuiCompressorParser();
    }
}
