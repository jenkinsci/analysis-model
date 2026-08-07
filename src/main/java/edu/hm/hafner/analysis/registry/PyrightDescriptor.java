package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.PyrightParser;

/**
 * A descriptor for Pyright static type checker JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/microsoft/pyright">Pyright on GitHub</a>
 */
class PyrightDescriptor extends ParserDescriptor {
    private static final String ID = "pyright";
    private static final String NAME = "Pyright";

    PyrightDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.WARNING;
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new PyrightParser();
    }

    @Override
    public String getPattern() {
        return "**/pyright-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>pyright --outputjson &gt; pyright-report.json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/microsoft/pyright'>Pyright on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/microsoft/pyright";
    }

    @Override
    public String getIconUrl() {
        return "https://github.com/microsoft/pyright/blob/main/docs/img/PyrightLarge.png";
    }
}
