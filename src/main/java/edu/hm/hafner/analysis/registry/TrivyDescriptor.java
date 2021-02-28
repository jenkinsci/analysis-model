package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.TrivyParser;

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
    public IssueParser createParser() {
        return new TrivyParser();
    }

    @Override
    public String getHelp() {
        return "Reads trivy json data. "
                + "Use commandline <code>trivy image -f json -o results.json 'image'</code>"
                + "See <a href='https://github.com/aquasecurity/trivy'>" + "tivy on Github</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/aquasecurity/trivy";
    }
}
