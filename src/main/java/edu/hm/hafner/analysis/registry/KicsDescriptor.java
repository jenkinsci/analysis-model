package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.KicsParser;

import static j2html.TagCreator.*;

/**
 * A descriptor for KICS JSON reports.
 *
 * @author Akash Manna
 */
class KicsDescriptor extends ParserDescriptor {
    private static final String ID = "kics";
    private static final String NAME = "KICS";

    KicsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new KicsParser();
    }

    @Override
    public String getPattern() {
        return "**/results.json";
    }

    @Override
    public String getHelp() {
        return join(text("Use commandline"),
                code("kics scan -p <path> -o ./ --report-formats json"),
                text("to generate results.json, see"),
                a("KICS").withHref("https://docs.kics.io/latest/"),
                text("for usage details.")).render();
    }
    
    @Override
    public String getIconUrl() {
        return "https://github.com/Checkmarx/kics/blob/master/docs/img/icon.svg?raw=true";
    }

    @Override
    public String getUrl() {
        return "https://docs.kics.io/latest/";
    }
}
