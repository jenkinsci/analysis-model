package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.FossaParser;

/**
 * A descriptor for FOSSA issue reports.
 *
 * @author Akash Manna
 */
class FossaDescriptor extends ParserDescriptor {
    private static final String ID = "fossa";
    private static final String NAME = "FOSSA";

    FossaDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    public String getUrl() {
        return "https://docs.fossa.com/";
    }

    @Override
    public String getIconUrl() {
        return "https://logo.svgcdn.com/logos/fossa.png";
    }

    @Override
    public String getPattern() {
        return "**/fossa-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>fossa test --format json &gt; fossa-report.json</code> to generate JSON output.<br/>"
                + "See <a href='https://docs.fossa.com/'>FOSSA documentation</a> for usage details.";
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new FossaParser();
    }
}