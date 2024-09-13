package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.YoctoScannerParser;

import static j2html.TagCreator.*;

/**
 * A descriptor for Yocto Scanner.
 *
 * @author Michael Trimarchi
 */
class YoctoScannerDescriptor extends ParserDescriptor {
    private static final String ID = "yoctocli";
    private static final String NAME = "Yocto Scanner";

    YoctoScannerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new YoctoScannerParser();
    }

    @Override
    public String getHelp() {
        return join(text("Use commandline"),
                code("bitbake <your product image>"),
                text(", add INHERIT += \"cve-check\" in your local.conf"),
                a("Yocto Scanner").withHref("https://docs.yoctoproject.org/dev/dev-manual/vulnerabilities.html"),
                text("for usage details.")).render();
    }

    @Override
    public String getUrl() {
        return "https://docs.yoctoproject.org/dev/dev-manual/vulnerabilities.html";
    }

    @Override
    public String getIconUrl() {
        return "https://www.yoctoproject.org/wp-content/uploads/sites/32/2023/09/YoctoProject_Logo_RGB_White_small.svg";
    }
}
