package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.KubeScoreParser;

/**
 * A descriptor for kube-score JSON reports.
 *
 * @author Akash Manna
 */
class KubeScoreDescriptor extends ParserDescriptor {
    private static final String ID = "kube-score";
    private static final String NAME = "KubeScore";

    KubeScoreDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new KubeScoreParser();
    }

    @Override
    public String getPattern() {
        return "**/kube-score.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>kube-score score -o json &gt; kube-score.json</code> output.<br/>"
                + "See <a href='https://github.com/zegl/kube-score'>kube-score on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/zegl/kube-score";
    }
}