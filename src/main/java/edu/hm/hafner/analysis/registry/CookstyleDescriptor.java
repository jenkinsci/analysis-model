package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.CookstyleParser;

/**
 * A descriptor for Cookstyle, Chef's RuboCop-based linter for Chef Infra cookbooks.
 *
 * @author Akash Manna
 */
class CookstyleDescriptor extends ParserDescriptor {
    private static final String ID = "cookstyle";
    private static final String NAME = "Cookstyle";

    CookstyleDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.WARNING;
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CookstyleParser();
    }

    @Override
    public String getPattern() {
        return "**/cookstyle-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>cookstyle --format json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/chef/cookstyle'>Cookstyle on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/chef/cookstyle";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/chef/chef-oss-practices/main/members/images/chef-logo.png";
    }
}
