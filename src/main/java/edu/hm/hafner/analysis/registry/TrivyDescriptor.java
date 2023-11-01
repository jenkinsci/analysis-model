package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.TrivyParser;

import static j2html.TagCreator.*;

/**
 * A descriptor for Aquasec Trivy.
 *
 * @author Lorenz Munsch
 */
public class TrivyDescriptor extends ParserDescriptor {
    private static final String ID = "trivy";
    private static final String NAME = "Aquasec Trivy";

    public TrivyDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new TrivyParser();
    }

    @Override
    public String getHelp() {
        return join(text("Use commandline"),
                code("trivy image -f json -o results.json 'image'"),
                text(", see"),
                a("tivy on Github").withHref("https://github.com/aquasecurity/trivy"),
                text("for usage details.")).render();
    }

    @Override
    public String getUrl() {
        return "https://github.com/aquasecurity/trivy";
    }

    @Override
    public String getIconUrl() {
        return "https://github.com/aquasecurity/trivy/blob/main/docs/imgs/logo.png?raw=true";
    }
}
