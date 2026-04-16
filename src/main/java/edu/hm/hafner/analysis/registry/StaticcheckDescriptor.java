package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.StaticcheckParser;

/**
 * A descriptor for Staticcheck JSON reports.
 *
 * @author Akash Manna
 */
class StaticcheckDescriptor extends ParserDescriptor {
    private static final String ID = "staticcheck";
    private static final String NAME = "Staticcheck";

    StaticcheckDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new StaticcheckParser();
    }

    @Override
    public String getPattern() {
        return "**/staticcheck-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>staticcheck -f json ./... > staticcheck-report.json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/dominikh/go-tools'>Staticcheck on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/dominikh/go-tools";
    }
}