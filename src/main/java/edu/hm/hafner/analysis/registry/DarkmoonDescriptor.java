package edu.hm.hafner.analysis.registry;

import static j2html.TagCreator.a;
import static j2html.TagCreator.br;
import static j2html.TagCreator.code;
import static j2html.TagCreator.join;
import static j2html.TagCreator.text;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.DarkmoonParser;

/**
 * A descriptor for the JSON findings reports of Darkmoon, an autonomous AI penetration testing platform.
 *
 * @see <a href="https://github.com/ASCIT31/Dark-Moon">Darkmoon</a>
 */
class DarkmoonDescriptor extends ParserDescriptor {
    private static final String ID = "darkmoon";
    private static final String NAME = "Darkmoon";

    DarkmoonDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new DarkmoonParser();
    }

    @Override
    public String getPattern() {
        return "**/darkmoon-*.json";
    }

    @Override
    public String getHelp() {
        return join(
                        text("Run the"),
                        a("Darkmoon GitHub Action").withHref("https://github.com/ASCIT31/darkmoon-action"),
                        text("with"),
                        code("report-format: json"),
                        text("to write the findings of an assessment to"),
                        code("darkmoon-&lt;campaign&gt;.json"),
                        text("."),
                        br(),
                        text("Findings carry the exploitation status, CVSS, CVE and MITRE ATT&amp;CK data."))
                .render();
    }

    @Override
    public String getUrl() {
        return "https://github.com/ASCIT31/Dark-Moon";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/ASCIT31/Dark-Moon/master/docs/pics/logo_blue.png";
    }
}
