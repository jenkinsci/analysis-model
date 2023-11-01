package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GccParser;

/**
 * A descriptor for GNU C Compiler 3 (gcc).
 *
 * @author Lorenz Munsch
 */
public class GccDescriptor extends ParserDescriptor {
    private static final String ID = "gcc3";
    private static final String NAME = "GNU C Compiler 3 (gcc)";

    public GccDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new GccParser();
    }
}
