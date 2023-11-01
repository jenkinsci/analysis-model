package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.ValgrindAdapter;

import static j2html.TagCreator.*;

/**
 * A descriptor for Valgrind.
 */
public class ValgrindDescriptor extends ParserDescriptor {
    private static final String ID = "valgrind";
    private static final String NAME = "Valgrind";

    public ValgrindDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new ValgrindAdapter();
    }

    @Override
    public String getHelp() {
        return join(text("Use options"),
                code("--xml=yes --xml-file=valgrind_report.xml --child-silent-after-fork=yes"),
                text(", see the"),
                a("Valgrind User Manual").withHref("https://valgrind.org/docs/manual/manual-core.html"),
                text("for usage details.")).render();
    }

    @Override
    public String getUrl() {
        return "https://valgrind.org";
    }

    @Override
    public String getIconUrl() {
        return "https://valgrind.org/images/valgrind-link3.png";
    }
}