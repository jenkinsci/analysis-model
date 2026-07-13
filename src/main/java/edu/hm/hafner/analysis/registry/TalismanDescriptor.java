package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.TalismanParser;

/**
 * A descriptor for Talisman security scanner JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/thoughtworks/talisman">Talisman on GitHub</a>
 */
class TalismanDescriptor extends ParserDescriptor {
    private static final String ID = "talisman";
    private static final String NAME = "Talisman";

    TalismanDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new TalismanParser();
    }

    @Override
    public String getPattern() {
        return "**/talisman-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>talisman --scanWithHtml</code> to generate a report that includes a JSON file.<br/>"
                + "See <a href='https://github.com/thoughtworks/talisman'>Talisman on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/thoughtworks/talisman";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/jaydeepc/talisman-html-report/master/img/talisman.png";
    }
}
