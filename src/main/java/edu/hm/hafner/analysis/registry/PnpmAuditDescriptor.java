package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PnpmAuditParser;

import static j2html.TagCreator.*;

/**
 * A descriptor for pnpm audit.
 *
 * @author Fabian Kaupp - kauppfbi@gmail.com
 */
class PnpmAuditDescriptor extends ParserDescriptor {
    private static final String ID = "pnpm-audit";
    private static final String NAME = "pnpm Audit";

    PnpmAuditDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new PnpmAuditParser();
    }

    @Override
    public String getHelp() {
        return join(text("Use commandline"),
                code("pnpm audit --json > pnpm-audit.json"),
                text(", see"),
                a("pnpm audit").withHref("https://pnpm.io/cli/audit"),
                text("for usage details.")).render();
    }

    @Override
    public String getUrl() {
        return "https://pnpm.io/cli/audit";
    }

    @Override
    public String getIconUrl() {
        return "https://pnpm.io/img/pnpm-no-name-with-frame.svg";
    }

    @Override
    public Type getType() {
        return Type.VULNERABILITY;
    }
}
