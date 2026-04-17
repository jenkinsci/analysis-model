package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PhanParser;

/**
 * A descriptor for Phan JSON reports.
 *
 * @author Akash Manna
 */
class PhanDescriptor extends ParserDescriptor {
    private static final String ID = "phan";
    private static final String NAME = "Phan";

    PhanDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new PhanParser();
    }

    @Override
    public String getPattern() {
        return "**/phan-report.json";
    }

    @Override
    public String getHelp() {
        return "Use <code>phan --output-mode json &gt; phan-report.json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/phan/phan'>Phan on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/phan/phan";
    }
}