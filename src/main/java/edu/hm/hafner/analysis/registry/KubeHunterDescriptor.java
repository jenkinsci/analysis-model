package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.KubeHunterParser;

/**
 * A descriptor for Kube Hunter JSON reports.
 *
 * @author Akash Manna
 */
class KubeHunterDescriptor extends ParserDescriptor {
    private static final String ID = "kube-hunter";
    private static final String NAME = "Kube Hunter";

    KubeHunterDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new KubeHunterParser();
    }

    @Override
    public String getPattern() {
        return "**/kube-hunter.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>kube-hunter --report json --dispatch stdout &gt; kube-hunter.json</code> "
                + "output.<br/>See <a href='https://github.com/aquasecurity/kube-hunter'>Kube Hunter on "
                + "GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/aquasecurity/kube-hunter";
    }
}
