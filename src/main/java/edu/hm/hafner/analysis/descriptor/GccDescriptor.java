package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.Gcc4LinkerParser;
import edu.hm.hafner.analysis.parser.GccParser;

/**
 * A Descriptor for the Gcc parser.
 *
 * @author Lorenz Munsch
 */
public class GccDescriptor extends ParserDescriptor {

    private static final String ID = "gcc";
    private static final String NAME = "Gcc";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public GccDescriptor() {
        super(ID, NAME, new GccParser());
    }
}
