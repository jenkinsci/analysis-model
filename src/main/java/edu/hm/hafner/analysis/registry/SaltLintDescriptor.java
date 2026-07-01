package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SaltLintParser;

/**
 * A descriptor for Salt Lint JSON reports.
 *
 * @author Akash Manna
 */
class SaltLintDescriptor extends ParserDescriptor {
    private static final String ID = "salt-lint";
    private static final String NAME = "Salt Lint";

    SaltLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new SaltLintParser();
    }

    @Override
    public String getPattern() {
        return "**/salt-lint-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>salt-lint --json</code> to generate JSON output.<br/>"
                + "Add <code>--severity</code> to include severity levels in the report.<br/>"
                + "See <a href='https://github.com/warpnet/salt-lint'>salt-lint on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/warpnet/salt-lint";
    }
}
