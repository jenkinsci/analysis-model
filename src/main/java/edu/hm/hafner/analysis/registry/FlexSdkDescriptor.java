package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.FlexSdkParser;

/**
 * A descriptor for FLEX SDK.
 *
 * @author Lorenz Munsch
 */
class FlexSdkDescriptor extends ParserDescriptor {
    private static final String ID = "flex";
    private static final String NAME = "Flex SDK Compiler";

    FlexSdkDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new FlexSdkParser();
    }
}
