package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.GitGuardianParser;

/**
 * A descriptor for GitGuardian (ggshield) JSON reports.
 *
 * @author Akash Manna
 */
class GitGuardianDescriptor extends ParserDescriptor {
    private static final String ID = "gitguardian";
    private static final String NAME = "GitGuardian";

    GitGuardianDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    public IssueParser create(final Option... options) {
        return new GitGuardianParser();
    }

    @Override
    public String getPattern() {
        return "**/gitguardian-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>ggshield secret scan path --recursive --json > gitguardian-report.json</code> "
                + "to generate JSON output.<br/>"
                + "See <a href='https://docs.gitguardian.com/ggshield-docs/reference/secret/scan/path'>"
                + "GitGuardian ggshield documentation</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://www.gitguardian.com/";
    }
}
