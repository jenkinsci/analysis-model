package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GosecParser;

/**
 * A descriptor for gosec JSON reports.
 *
 * @author Akash Manna
 */
class GosecDescriptor extends ParserDescriptor {
    private static final String ID = "gosec";
    private static final String NAME = "gosec";

    GosecDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new GosecParser();
    }

    @Override
    public String getPattern() {
        return "**/gosec-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>gosec -fmt=json -out=gosec-report.json ./...</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/securego/gosec'>gosec on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/securego/gosec";
    }
}