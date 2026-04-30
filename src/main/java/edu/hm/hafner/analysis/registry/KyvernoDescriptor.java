package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.KyvernoParser;

/**
 * A descriptor for Kyverno policy validation JSON reports (OpenReports format).
 *
 * @author Akash Manna
 */
class KyvernoDescriptor extends ParserDescriptor {
    private static final String ID = "kyverno";
    private static final String NAME = "Kyverno";

    KyvernoDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new KyvernoParser();
    }

    @Override
    public String getPattern() {
        return "**/kyverno-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>kyverno apply policy.yaml resource.yaml --output json</code> to generate JSON output.<br/>"
                + "See <a href='https://kyverno.io/docs/kyverno-cli/commands/apply/'>Kyverno CLI documentation</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://kyverno.io/";
    }
}
