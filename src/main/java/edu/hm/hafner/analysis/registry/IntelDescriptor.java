package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.IntelParser;

/**
 * A descriptor for Intel compilers.
 *
 * @author Lorenz Munsch
 */
class IntelDescriptor extends ParserDescriptor {
    private static final String ID = "intel";
    private static final String NAME = "Intel Compiler (C, Fortran)";

    IntelDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new IntelParser();
    }
}
