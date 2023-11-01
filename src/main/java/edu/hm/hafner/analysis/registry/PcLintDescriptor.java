package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.MsBuildParser;

/**
 * A descriptor for the PC-Lint Tool.
 *
 * @author Lorenz Munsch
 */
public class PcLintDescriptor extends ParserDescriptor {
    private static final String ID = "pclint";
    private static final String NAME = "PC-Lint Tool";

    public PcLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new MsBuildParser();
    }

    @Override
    public String getHelp() {
        return "<p>Use the following PC-Lint properties to create an output file in the correct format: <pre><code>"
                + "-v // turn off verbosity\n"
                + "-width(0) // don't insert line breaks (unlimited output width)\n"
                + "-\"format=%f(%l): %t %n: %m\"\n"
                + "-hs1 // The height of a message should be 1\n"
                + "</code></pre></p>";
    }
}
