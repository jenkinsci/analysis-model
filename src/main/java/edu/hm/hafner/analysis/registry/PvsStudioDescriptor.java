package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.pvsstudio.PVSStudioParser;

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
    public IssueParser createParser(final Option... options) {
        return new PVSStudioParser();
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
