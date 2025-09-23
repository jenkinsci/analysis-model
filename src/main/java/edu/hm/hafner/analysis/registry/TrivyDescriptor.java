package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.TrivyParser;

import static j2html.TagCreator.*;

/**
 * A descriptor for Aquasec Trivy.
 *
 * @author Lorenz Munsch
 */
class TrivyDescriptor extends ParserDescriptor {
    private static final String ID = "trivy";
    private static final String NAME = "Aquasec Trivy";

    TrivyDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new TrivyParser();
    }

    @Override
    public String getHelp() {
        return join(text("Use commandline"),
                code("trivy image -f json -o results.json 'image'"),
                text(", see"),
                a("tivy on Github").withHref("https://github.com/aquasecurity/trivy"),
                text("for usage details."),
                p("Supported scanners:"),
                ul().with(
                    li(a("Vulnerability Scanner").withHref("https://trivy.dev/latest/docs/scanner/vulnerability/")),
                    li(a("Misconfiguration Scanner").withHref("https://trivy.dev/latest/docs/scanner/misconfiguration/"))
                )).render();
    }

    @Override
    public String getUrl() {
        return "https://github.com/aquasecurity/trivy";
    }

    @Override
    public String getIconUrl() {
        return "https://github.com/aquasecurity/trivy/blob/main/docs/imgs/logo.png?raw=true";
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }
}
