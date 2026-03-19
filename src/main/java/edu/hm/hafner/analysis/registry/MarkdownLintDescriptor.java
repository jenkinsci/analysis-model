package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.MarkdownLintParser;

/**
 * A descriptor for markdownlint JSON reports.
 * 
 * @author Akash Manna
 */
class MarkdownLintDescriptor extends ParserDescriptor {
    private static final String ID = "markdownlint";
    private static final String NAME = "MarkdownLint";

    MarkdownLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new MarkdownLintParser();
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>markdownlint-cli2 --json</code> output.<br/>"
                + "See <a href='https://github.com/DavidAnson/markdownlint'>markdownlint on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/DavidAnson/markdownlint";
    }
}