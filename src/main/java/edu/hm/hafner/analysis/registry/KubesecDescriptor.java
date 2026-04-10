package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.KubesecParser;

/**
 * A descriptor for Kubesec JSON reports.
 *
 * @author Akash Manna
 */
class KubesecDescriptor extends ParserDescriptor {
    private static final String ID = "kubesec";
    private static final String NAME = "Kubesec";

    KubesecDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new KubesecParser();
    }

    @Override
    public String getPattern() {
        return "**/kubesec.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>kubesec scan -f deployment.yaml -o json > kubesec.json</code> output.<br/>"
                + "See <a href='https://kubesec.io/'>Kubesec</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://kubesec.io/";
    }
}
