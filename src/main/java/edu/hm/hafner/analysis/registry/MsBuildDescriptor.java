package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.MsBuildParser;

/**
 * A descriptor for MS Build messages.
 *
 * @author Lorenz Munsch
 */
class MsBuildDescriptor extends ParserDescriptor {
    private static final String ID = "msbuild";
    private static final String NAME = "MSBuild";

    MsBuildDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new MsBuildParser();
    }

    @Override
    public String getUrl() {
        return "https://github.com/dotnet/msbuild";
    }
}
