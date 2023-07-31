package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CrossCoreEmbeddedStudioParser;

/**
 * A descriptor for CrossCore Embedded Studio from Analog Devices.
 */
public class CrossCoreEmbeddedStudioDescriptor extends ParserDescriptor {
    private static final String ID = "crosscore-embedded-studio";
    private static final String NAME = "CrossCore Embedded Studio (CCES)";

    CrossCoreEmbeddedStudioDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CrossCoreEmbeddedStudioParser();
    }

    @Override
    public String getUrl() {
        return "https://www.analog.com/en/design-center/evaluation-hardware-and-software/software/adswt-cces.html";
    }
}
