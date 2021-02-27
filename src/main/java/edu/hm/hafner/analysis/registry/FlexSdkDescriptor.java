package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.FlexSdkParser;

/**
 * A Descriptor for the Flex SDK parser.
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
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new FlexSdkParser();
    }
}
