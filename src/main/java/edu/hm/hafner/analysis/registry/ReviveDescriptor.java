package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ReviveParser;

/**
 * A descriptor for Revive JSON reports.
 *
 * @author Akash Manna
 */
class ReviveDescriptor extends ParserDescriptor {
    private static final String ID = "revive";
    private static final String NAME = "Revive";

    ReviveDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new ReviveParser();
    }

    @Override
    public String getPattern() {
        return "**/revive-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>revive -formatter json ./...</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/mgechev/revive'>Revive on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/mgechev/revive";
    }
}
