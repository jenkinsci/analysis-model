package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PuppetLintParser;

/**
 * A descriptor for the Puppet Lint.
 *
 * @author Lorenz Munsch
 */
class PuppetLintDescriptor extends ParserDescriptor {
    private static final String ID = "puppetlint";
    private static final String NAME = "Puppet Lint";

    PuppetLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new PuppetLintParser();
    }

    @Override
    public String getHelp() {
        return "  <p>\n"
                + "    To be able to grab puppet-lint output, you will need a recent enough version that supports\n"
                + "    the <code>--log-format flag</code>.<br/>\n"
                + "    When running puppet-lint, make sure you use the following log format:\n"
                + "  </p>\n"
                + "  <pre>\n"
                + "    <code>%{path}:%{line}:%{check}:%{KIND}:%{message}</code>\n"
                + "  </pre>\n"
                + "  <p>Complete example:</p>\n"
                + "  <pre>\n"
                + "      <code>find . -iname *.pp -exec puppet-lint --log-format \"%{path}:%{line}:%{check}:%{KIND}:%{message}\" {} \\;</code>\n"
                + "  </pre>\n";
    }
}
