package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.DetectSecretsParser;

/**
 * A descriptor for detect-secrets secret scanning reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/Yelp/detect-secrets">detect-secrets on GitHub</a>
 */
class DetectSecretsDescriptor extends ParserDescriptor {
    private static final String ID = "detect-secrets";
    private static final String NAME = "detect-secrets";

    DetectSecretsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    public IssueParser create(final Option... options) {
        return new DetectSecretsParser();
    }

    @Override
    public String getPattern() {
        return "**/.secrets.baseline";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>detect-secrets scan &gt; .secrets.baseline</code> to generate the baseline JSON report.<br/>"
                + "See <a href='https://github.com/Yelp/detect-secrets'>detect-secrets on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/Yelp/detect-secrets";
    }
}
