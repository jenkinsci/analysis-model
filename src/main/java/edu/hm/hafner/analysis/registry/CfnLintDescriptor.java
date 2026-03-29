package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CfnLintParser;

/**
 * A descriptor for CFN-Lint JSON reports.
 *
 * @author Akash Manna
 */
class CfnLintDescriptor extends ParserDescriptor {
    private static final String ID = "cfn-lint";
    private static final String NAME = "CFN-Lint";

    CfnLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CfnLintParser();
    }

    @Override
    public String getPattern() {
        return "**/cfn-lint-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>cfn-lint --format json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/aws-cloudformation/cfn-lint'>cfn-lint on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/aws-cloudformation/cfn-lint";
    }
}