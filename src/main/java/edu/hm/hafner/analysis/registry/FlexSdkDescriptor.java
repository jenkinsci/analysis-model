package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.FlexSdkParser;

/**
 * A descriptor for FLEX SDK.
 *
 * @author Lorenz Munsch
 */
public class FlexSdkDescriptor extends ParserDescriptor {
    private static final String ID = "flex";
    private static final String NAME = "Flex SDK Compiler";

    public FlexSdkDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new FlexSdkParser();
    }
}
