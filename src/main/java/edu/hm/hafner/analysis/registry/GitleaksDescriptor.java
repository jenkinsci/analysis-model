package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.GitleaksParser;

/**
 * Descriptor for Gitleaks JSON reports.
 * 
 * @author Akash Manna
 */
class GitleaksDescriptor extends ParserDescriptor {
    private static final String ID = "gitleaks";
    private static final String NAME = "Gitleaks";

    GitleaksDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    public IssueParser create(final Option... options) {
        return new GitleaksParser();
    }

    @Override
    public String getPattern() {
        return "**/gitleaks.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>gitleaks detect --report-format json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/zricethezav/gitleaks'>gitleaks on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/zricethezav/gitleaks";
    }
}
