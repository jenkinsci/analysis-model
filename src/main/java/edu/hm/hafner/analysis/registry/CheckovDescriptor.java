package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CheckovParser;

/**
 * A descriptor for Checkov JSON reports.
 *
 * @author Akash Manna
 */
class CheckovDescriptor extends ParserDescriptor {
    private static final String ID = "checkov";
    private static final String NAME = "Checkov";

    CheckovDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CheckovParser();
    }

    @Override
    public String getPattern() {
        return "**/checkov-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>checkov --output json --output-file-path checkov-report.json</code> "
                + "to generate JSON output.<br/>"
                + "See <a href='https://www.checkov.io/'>Checkov documentation</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://www.checkov.io/";
    }
}