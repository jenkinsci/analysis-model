package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.AnchoreCtlParser;

import static j2html.TagCreator.*;

/**
 * Descriptor for the AnchoreCTL vulnerability report parser.
 */
class AnchoreCtlDescriptor extends ParserDescriptor {
    private static final String ID = "anchore-ctl";
    private static final String NAME = "AnchoreCTL";

    AnchoreCtlDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new AnchoreCtlParser();
    }

    @Override
    public String getPattern() {
        return "**/*vulnerabilities*.json";
    }

    @Override
    public String getUrl() {
        return "https://docs.anchore.com/current/docs/using/cli_usage/images/";
    }

    @Override
    public String getHelp() {
        return join(
                text("Use commandline"),
                code("anchorectl image one-time-scan -o json IMAGE > anchorectl-scan.json"),
                text(", see"),
                a("anchorectl documentation").withHref(getUrl()),
                text("for usage details.")
        ).render();
    }
}
