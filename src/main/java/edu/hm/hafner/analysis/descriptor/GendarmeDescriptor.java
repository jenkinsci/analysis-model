package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.gendarme.GendarmeParser;

/**
 * A Descriptor for the Gendarme warnings.
 *
 * @author Lorenz Munsch
 */
public class GendarmeDescriptor extends ParserDescriptor {

    private static final String ID = "gendarme";
    private static final String NAME = "Gendarme";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public GendarmeDescriptor() {
        super(ID, NAME, new GendarmeParser());
    }
}
