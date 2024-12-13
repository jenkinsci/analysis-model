package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GnuFortranParser;

/**
 * A descriptor for the Gnu Fortran Compiler.
 *
 * @author Lorenz Munsch
 */
class GnuFortranDescriptor extends ParserDescriptor {
    private static final String ID = "fortran";
    private static final String NAME = "GNU Fortran Compiler";

    GnuFortranDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new GnuFortranParser();
    }
}
