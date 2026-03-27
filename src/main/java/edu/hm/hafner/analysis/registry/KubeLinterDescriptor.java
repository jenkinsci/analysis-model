package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.KubeLinterParser;

/**
 * A descriptor for KubeLinter JSON reports.
 *
 * @author Akash Manna
 */
class KubeLinterDescriptor extends ParserDescriptor {
    private static final String ID = "kube-linter";
    private static final String NAME = "KubeLinter";

    KubeLinterDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new KubeLinterParser();
    }

    @Override
    public String getPattern() {
        return "**/kube-linter.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>kube-linter lint --format json --output kube-linter.json</code> output.<br/>"
                + "See <a href='https://github.com/stackrox/kube-linter'>KubeLinter on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/stackrox/kube-linter";
    }
}
