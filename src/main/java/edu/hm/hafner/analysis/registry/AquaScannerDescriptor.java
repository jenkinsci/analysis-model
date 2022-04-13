package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.AquaScannerParser;

import static j2html.TagCreator.*;

/**
 * A descriptor for Aqua Scanner.
 *
 * @author Juri Duval
 */
class AquaScannerDescriptor extends ParserDescriptor {
    private static final String ID = "scannercli";
    private static final String NAME = "Aqua Scanner";

    AquaScannerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new AquaScannerParser();
    }

    @Override
    public String getHelp() {
        return join(text("Use commandline"),
                code("scannercli scan 'image' --jsonfile results.json"),
                text(", see"),
                a("Aqua Scanner CLI").withHref("https://support.aquasec.com/support/solutions/articles/16000120206"),
                text("for usage details.")).render();
    }
}
