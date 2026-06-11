package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.FortifySscParser;

/**
 * A descriptor for Fortify Software Security Center (SSC) parser.
 *
 * @author Your Name
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
