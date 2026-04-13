package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CfnNagParser;

/**
 * A descriptor for CFN-Nag JSON reports.
 *
 * @author Akash Manna
 */
class CfnNagDescriptor extends ParserDescriptor {
    private static final String ID = "cfn-nag";
    private static final String NAME = "CFN-Nag";

    CfnNagDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CfnNagParser();
    }

    @Override
    public String getPattern() {
        return "**/cfn-nag-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>cfn_nag_scan --input-path . --output-format json</code> "
                + "to generate JSON output.<br/>"
                + "See <a href='https://github.com/stelligent/cfn_nag'>cfn_nag on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/stelligent/cfn_nag";
    }
}