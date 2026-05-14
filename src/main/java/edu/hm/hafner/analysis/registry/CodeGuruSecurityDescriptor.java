package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.CodeGuruSecurityParser;

/**
 * A descriptor for AWS CodeGuru Security.
 *
 * @author Akash Manna
 */
class CodeGuruSecurityDescriptor extends ParserDescriptor {
    private static final String ID = "codeguru-security";
    private static final String NAME = "AWS CodeGuru Security";

    CodeGuruSecurityDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CodeGuruSecurityParser();
    }

    @Override
    public String getPattern() {
        return "**/codeguru-security-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>aws codeguru-security get-findings --scan-name &lt;scan-name&gt; --output json "
                + "> codeguru-security-report.json</code> to generate JSON output.<br/>"
                + "See <a href='https://docs.aws.amazon.com/cli/latest/reference/codeguru-security/get-findings.html'>"
                + "AWS CodeGuru Security get-findings</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://docs.aws.amazon.com/cli/latest/reference/codeguru-security/get-findings.html";
    }
}