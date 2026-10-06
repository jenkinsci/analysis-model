package edu.hm.hafner.analysis.registry;

import static j2html.TagCreator.a;
import static j2html.TagCreator.code;
import static j2html.TagCreator.join;
import static j2html.TagCreator.text;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.parser.NpmAuditParser;

/**
 * A descriptor for npm audit.
 *
 * @author Ulrich Grave
 */
class NpmAuditDescriptor extends ParserDescriptor {
    private static final String ID = "npm-audit";
    private static final String NAME = "npm Audit";

    NpmAuditDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new NpmAuditParser();
    }

    @Override
    public String getHelp() {
        return join(
                        text("Use commandline"),
                        code("npm audit --json > npm-audit.json"),
                        text(", see"),
                        a("npm audit").withHref("https://docs.npmjs.com/cli/commands/npm-audit"),
                        text("for usage details."))
                .render();
    }

    @Override
    public String getUrl() {
        return "https://docs.npmjs.com/cli/commands/npm-audit";
    }

    @Override
    public Report.IssueType getType() {
        return Report.IssueType.VULNERABILITY;
    }
}
