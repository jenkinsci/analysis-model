package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.OpenScapParser;

/**
 * A descriptor for OpenSCAP compliance and vulnerability scanner.
 *
 * @author Akash Manna
 */
class OpenScapDescriptor extends ParserDescriptor {
    private static final String ID = "openscap";
    private static final String NAME = "OpenSCAP";

    OpenScapDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.WARNING;
    }

    @Override
    public String getIconUrl() {
        return "https://github.com/OpenSCAP/openscap/blob/main/docs/manual/images/vertical-logo.png";
    }

    @Override
    public IssueParser create(final Option... options) {
        return new OpenScapParser();
    }

    @Override
    public String getPattern() {
        return "**/openscap-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>oscap scan --results-arf results.xml --report report.html</code> "
                + "to generate reports. Convert ARF to JSON for automated parsing.<br/>"
                + "See <a href='https://github.com/OpenSCAP/openscap'>OpenSCAP on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/OpenSCAP/openscap";
    }
}
