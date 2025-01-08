package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PvsStudioParser;

/**
 * A descriptor for the PVS-Studio static analyzer.
 *
 * @author Lorenz Munsch
 */
class PvsStudioDescriptor extends ParserDescriptor {
    private static final String ID = "pvs-studio";
    private static final String NAME = "PVS-Studio";

    PvsStudioDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new PvsStudioParser();
    }

    @Override
    public String getPattern() {
        return "**/*.plog";
    }

    @Override
    public String getUrl() {
        return "https://pvs-studio.com/en/pvs-studio/";
    }
}
