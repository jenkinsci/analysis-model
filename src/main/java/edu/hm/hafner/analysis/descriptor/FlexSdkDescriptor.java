package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.FindBugsParser;
import edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty;
import edu.hm.hafner.analysis.parser.FlexSdkParser;

/**
 * A Descriptor for the Flex SDK parser.
 *
 * @author Lorenz Munsch
 */
public class FlexSdkDescriptor extends ParserDescriptor {

    private static final String ID = "flex_sdk";
    private static final String NAME = "FlexSdk";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public FlexSdkDescriptor() {
        super(ID, NAME, new FlexSdkParser());
    }
}
