package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.FortifySscParser;

/**
 * A descriptor for Fortify Software Security Center (SSC) JSON reports.
 *
 * @see <a href="https://www.microfocus.com/documentation/fortify-software-security-center/">Fortify Software Security Center Documentation</a>
 * @author Akash Manna
 */
class FortifySscDescriptor extends ParserDescriptor {
    private static final String ID = "fortifyssc";
    private static final String NAME = "Fortify SSC";

    FortifySscDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    public String getUrl() {
        return "https://www.microfocus.com/en-us/cyberres/application-security/software-security-center";
    }

    @Override
    public String getPattern() {
        return "**/fortifyssc.json";
    }

    @Override
    public IssueParser create(final Option... options) {
        return new FortifySscParser();
    }
}
