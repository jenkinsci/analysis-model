package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.OTDockerLintParser;

/**
 * A descriptor for {@code ot-docker-lint} json report.
 *
 * @author Lorenz Munsch
 */
public class OtDockerLintDescriptor extends ParserDescriptor {
    private static final String ID = "ot-docker-linter";
    private static final String NAME = "OT Docker Linter";

    public OtDockerLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new OTDockerLintParser();
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>ot-docker-linter audit --docker.file Dockerfile -o json</code> output.<br/>"
                + "See <a href='https://github.com/opstree/OT-Dockerlinter'>ot-docker-linter on Github</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/opstree/OT-Dockerlinter";
    }
}
