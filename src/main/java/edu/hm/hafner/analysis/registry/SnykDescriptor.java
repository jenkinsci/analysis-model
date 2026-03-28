package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.SnykParser;

/**
 * A descriptor for Snyk security vulnerability scanner.
 *
 * @author Akash Manna
 */
class SnykDescriptor extends ParserDescriptor {
    private static final String ID = "snyk";
    private static final String NAME = "Snyk";

    SnykDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    public String getUrl() {
        return "https://snyk.io/";
    }

    @Override
    public String getIconUrl() {
        return "https://logo.svgcdn.com/logos/snyk.png";
    }

    @Override
    public String getPattern() {
        return "**/snyk-report.json";
    }

    @Override
    public IssueParser create(final Option... options) {
        return new SnykParser();
    }
}
