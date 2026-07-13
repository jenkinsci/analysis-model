package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.BlackParser;

/**
 * A descriptor for Black Python code formatter reports.
 *
 * @author Akash Manna
 */
class BlackDescriptor extends ParserDescriptor {
    private static final String ID = "black";
    private static final String NAME = "Black";

    BlackDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.WARNING;
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new BlackParser();
    }

    @Override
    public String getPattern() {
        return "**/black-report.txt";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>black --check . 2&gt;&amp;1 | tee black-report.txt</code> to capture output.<br/>"
                + "See <a href='https://black.readthedocs.io/'>Black documentation</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/psf/black";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/psf/black/main/docs/_static/logo2-readme.png";
    }
}
