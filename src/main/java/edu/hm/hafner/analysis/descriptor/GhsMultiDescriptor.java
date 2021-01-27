package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.GccParser;
import edu.hm.hafner.analysis.parser.GhsMultiParser;

/**
 * A Descriptor for the GHS Multi parser.
 *
 * @author Lorenz Munsch
 */
public class GhsMultiDescriptor extends ParserDescriptor {

    private static final String ID = "ghs-multi";
    private static final String NAME = "GHS Multi Compiler";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public GhsMultiDescriptor() {
        super(ID, NAME, new GhsMultiParser());
    }
}
