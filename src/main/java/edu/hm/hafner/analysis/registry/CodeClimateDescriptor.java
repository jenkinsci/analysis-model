package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.CodeClimateAdapter;

/**
 * A descriptor for the Code Climate parser.
 *
 */
class CodeClimateDescriptor extends ParserDescriptor {
    private static final String ID = "code-climate";
    private static final String NAME = "Code Climate";

    CodeClimateDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CodeClimateAdapter();
    }

    @Override
    public String getUrl() {
        return "https://codeclimate.com/";
    }
}
