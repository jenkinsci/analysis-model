package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.RuffParser;

/**
 * A descriptor for Ruff JSON reports.
 *
 * @author Akash Manna
 */
class RuffDescriptor extends ParserDescriptor {
    private static final String ID = "ruff";
    private static final String NAME = "Ruff";

    RuffDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new RuffParser();
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>ruff check --output-format=json</code> to generate the JSON report.<br/>"
                + "See <a href='https://docs.astral.sh/ruff/'>Ruff Documentation</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/astral-sh/ruff";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/astral-sh/ruff/main/docs/assets/bolt.svg";
    }
}
