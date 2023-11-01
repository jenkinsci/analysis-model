package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.DockerLintParser;

/**
 * A descriptor for {@code dockerfile_lint} json report.
 *
 * @author Lorenz Munsch
 */
public class DockerLintDescriptor extends ParserDescriptor {
    private static final String ID = "dockerlint";
    private static final String NAME = "Dockerfile Lint";

    public DockerLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new DockerLintParser();
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>dockerfile_lint -j</code> output.<br/>"
                + "See <a href='https://github.com/projectatomic/dockerfile_lint'>"
                + "dockerfile_lint on Github</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/projectatomic/dockerfile_lint";
    }
}
