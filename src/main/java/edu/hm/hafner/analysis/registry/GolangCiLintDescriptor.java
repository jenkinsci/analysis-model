package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GolangCiLintParser;

/**
 * A descriptor for golangci-lint JSON reports.
 *
 * @author Akash Manna
 */
class GolangCiLintDescriptor extends ParserDescriptor {
    private static final String ID = "golangci-lint";
    private static final String NAME = "golangci-lint";

    GolangCiLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new GolangCiLintParser();
    }

    @Override
    public String getPattern() {
        return "**/golangci-lint-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>golangci-lint run --output.json.path=golangci-lint-report.json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/golangci/golangci-lint'>golangci-lint on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/golangci/golangci-lint";
    }
}