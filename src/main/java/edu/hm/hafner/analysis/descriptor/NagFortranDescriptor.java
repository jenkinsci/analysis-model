package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.NagFortranParser;

/**
 * A Descriptor for the Nag Fortran parser.
 *
 * @author Lorenz Munsch
 */
class NagFortranDescriptor extends ParserDescriptor {

    private static final String ID = "nag-fortran";
    private static final String NAME = "NAG Fortran Compiler";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    NagFortranDescriptor() {
        super(ID, NAME, new NagFortranParser());
    }
}
