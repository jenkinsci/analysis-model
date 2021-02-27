package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.NagFortranParser;

/**
 * A Descriptor for the Nag Fortran parser.
 *
 * @author Lorenz Munsch
 */
class NagFortranDescriptor extends ParserDescriptor {
    private static final String ID = "nag-fortran";
    private static final String NAME = "NAG Fortran Compiler";

    NagFortranDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new NagFortranParser();
    }
}
