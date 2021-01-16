package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.GnatParser;
import edu.hm.hafner.analysis.parser.GnuFortranParser;

/**
 * A Descriptor for the Gnu Fortran parser.
 *
 * @author Lorenz Munsch
 */
public class GnuFortranDescriptor extends ParserDescriptor {

    private static final String ID = "gnu_fortran";
    private static final String NAME = "GnuFortran";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public GnuFortranDescriptor() {
        super(ID, NAME, new GnuFortranParser());
    }
}
