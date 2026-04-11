package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.TfsecParser;

/**
 * A descriptor for tfsec JSON reports.
 *
 * @author Akash Manna
 */
class TfsecDescriptor extends ParserDescriptor {
    private static final String ID = "tfsec";
    private static final String NAME = "tfsec";

    TfsecDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new TfsecParser();
    }

    @Override
    public String getPattern() {
        return "**/tfsec-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>tfsec . -f json -o tfsec-report.json</code> to generate JSON output.<br/>"
                + "See <a href='https://aquasecurity.github.io/tfsec/latest/'>tfsec documentation</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://aquasecurity.github.io/tfsec/";
    }
}
