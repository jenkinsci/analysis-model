package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.fxcop.FxCopParser;

/**
 * A Descriptor for the Fxcop warnings.
 *
 * @author Lorenz Munsch
 */
public class FxcopDescriptor extends ParserDescriptor {

    private static final String ID = "fxcop";
    private static final String NAME = "FxCop";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public FxcopDescriptor() {
        super(ID, NAME, new FxCopParser());
    }
}
