package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.OsvScannerParser;

/**
 * A descriptor for OSV-Scanner vulnerability reports.
 *
 * @author Akash Manna
 */
class OsvScannerDescriptor extends ParserDescriptor {
    private static final String ID = "osv-scanner";
    private static final String NAME = "OSV-Scanner";

    OsvScannerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new OsvScannerParser();
    }

    @Override
    public String getPattern() {
        return "**/osv-scanner-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>osv-scanner scan --format json /path/to/project &gt; osv-scanner-report.json</code>"
                + " to generate a JSON report.<br/>"
                + "See <a href='https://google.github.io/osv-scanner/'>OSV-Scanner documentation</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://google.github.io/osv-scanner/";
    }

    @Override
    public String getIconUrl() {
        return "https://google.github.io/osv-scanner/assets/icon.png";
    }
}
