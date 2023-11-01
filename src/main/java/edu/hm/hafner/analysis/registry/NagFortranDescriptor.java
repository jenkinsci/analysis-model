package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.NagFortranParser;

/**
 * A descriptor for the NagFortran Compiler.
 *
 * @author Lorenz Munsch
 */
public class NagFortranDescriptor extends ParserDescriptor {
    private static final String ID = "nag-fortran";
    private static final String NAME = "NAG Fortran Compiler";

    public NagFortranDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new NagFortranParser();
    }
}
