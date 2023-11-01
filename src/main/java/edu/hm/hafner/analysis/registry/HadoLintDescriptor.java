package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.HadoLintParser;

/**
 * A descriptor for {@code hadolint} json report.
 *
 * @author Lorenz Munsch
 */
public class HadoLintDescriptor extends ParserDescriptor {
    private static final String ID = "hadolint";
    private static final String NAME = "HadoLint";

    public HadoLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new HadoLintParser();
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>hadolint --format json Dockerfile</code> output.<br/>"
                + "See <a href='https://github.com/hadolint/hadolint'>hadolint on Github</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/hadolint/hadolint";
    }
}
