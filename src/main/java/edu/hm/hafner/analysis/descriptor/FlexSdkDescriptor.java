package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.FlexSdkParser;

/**
 * A Descriptor for the Flex SDK parser.
 *
 * @author Lorenz Munsch
 */
class FlexSdkDescriptor extends ParserDescriptor {

    private static final String ID = "flex";
    private static final String NAME = "Flex SDK Compiler";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    FlexSdkDescriptor() {
        super(ID, NAME, new FlexSdkParser());
    }
}
