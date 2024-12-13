package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PuppetLintParser;

import static j2html.TagCreator.*;

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
    public IssueParser create(final Option... options) {
        return new PuppetLintParser();
    }

    @Override
    public String getHelp() {
        return join(text("You will need a recent enough version that supports"),
                code("--log-format flag"),
                text(". When running puppet-lint, make sure you use the log format"),
                code("%{path}:%{line}:%{check}:%{KIND}:%{message}"),
                text("."),
                br(),
                text("Complete example:"),
                br(),
                code("find . -iname *.pp -exec puppet-lint --log-format \"%{path}:%{line}:%{check}:%{KIND}:%{message}\" {} \\;"))
                .render();
    }
}
