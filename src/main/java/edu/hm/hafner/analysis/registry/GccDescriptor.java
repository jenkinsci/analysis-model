package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GccParser;

/**
 * A descriptor for legacy GNU C Compiler (gcc) versions older than GCC 4.
 *
 * <p>
 * This parser is designed for very old GCC compilers (pre-GCC 4) that use a different output format. 
 * For modern GCC versions (4 and newer, including GCC 5–15), use the "gcc" parser instead.
 * </p>
 *
 * @author Lorenz Munsch
 */
class GccDescriptor extends ParserDescriptor {
    private static final String ID = "gcc3";
    private static final String NAME = "GNU C Compiler (Legacy, pre-GCC 4)";

    GccDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new GccParser();
    }

    @Override
    public String getHelp() {
        return "<p>Parses warnings and errors from legacy GCC compilers (versions older than GCC 4). "
                + "This parser uses an older, simpler warning format.</p>"
                + "<p>For modern GCC versions (GCC 4 and newer, including GCC 5–15), use the 'gcc' parser instead, "
                + "which supports the newer format with additional context like:</p>"
                + "<p><code>file.c:10:5: warning: unused variable 'x' [-Wunused-variable]</code></p>";
    }
}
