package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.TruffleHogParser;

/**
 * A descriptor for TruffleHog secret detection scanner.
 *
 * @author Akash Manna
 */
class TruffleHogDescriptor extends ParserDescriptor {
    private static final String ID = "truffleHog";
    private static final String NAME = "TruffleHog";

    TruffleHogDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    public String getUrl() {
        return "https://github.com/trufflesecurity/trufflehog";
    }

    @Override
    public String getIconUrl() {
        return "https://storage.googleapis.com/trufflehog-static-sources/pixel_pig.png";
    }

    @Override
    public String getPattern() {
        return "**/truffleHog.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>truffleHog filesystem /path --json --only-verified</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/trufflesecurity/trufflehog'>TruffleHog on GitHub</a> for usage details.";
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new TruffleHogParser();
    }
}
