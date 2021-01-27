package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.IdeaInspectionParser;
import edu.hm.hafner.analysis.parser.IntelParser;

/**
 * A Descriptor for the Intel parser.
 *
 * @author Lorenz Munsch
 */
public class IntelDescriptor extends ParserDescriptor {

    private static final String ID = "intel";
    private static final String NAME = "Intel Compiler (C, Fortran)";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public IntelDescriptor() {
        super(ID, NAME, new IntelParser());
    }
}
