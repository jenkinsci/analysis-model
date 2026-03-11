package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ShellCheckParser;

/**
 * A descriptor for ShellCheck JSON reports.
 *
 * @author Akash Manna
 */
class ShellCheckDescriptor extends ParserDescriptor {
    private static final String ID = "shellcheck";
    private static final String NAME = "ShellCheck";

    ShellCheckDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new ShellCheckParser();
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>shellcheck -f json script.sh</code> to generate the JSON report.<br/>"
                + "See <a href='https://github.com/koalaman/shellcheck'>ShellCheck Documentation</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/koalaman/shellcheck";
    }

    @Override
    public String getPattern() {
        return "**/shellcheck.json";
    }
}
