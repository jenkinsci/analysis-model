package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.fxcop.FxCopParser;

/**
 * A Descriptor for the Fxcop warnings.
 *
 * @author Lorenz Munsch
 */
class FxcopDescriptor extends ParserDescriptor {
    private static final String ID = "fxcop";
    private static final String NAME = "FxCop";

    FxcopDescriptor() {
        super(ID, NAME, new FxCopParser());
    }
}
