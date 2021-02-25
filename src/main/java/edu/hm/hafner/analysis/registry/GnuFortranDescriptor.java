package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.GnuFortranParser;

/**
 * A Descriptor for the Gnu Fortran parser.
 *
 * @author Lorenz Munsch
 */
class GnuFortranDescriptor extends ParserDescriptor {
    private static final String ID = "fortran";
    private static final String NAME = "GNU Fortran Compiler";

    GnuFortranDescriptor() {
        super(ID, NAME, new GnuFortranParser());
    }
}
