package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.CoolfluxChessccParser;

/**
 * A Descriptor for the Coold Flux Chesscc parser.
 *
 * @author Lorenz Munsch
 */
class CoolfluxChessccDescriptor extends ParserDescriptor {
    private static final String ID = "coolflux";
    private static final String NAME = "Coolflux DSP Compiler";

    CoolfluxChessccDescriptor() {
        super(ID, NAME, new CoolfluxChessccParser());
    }
}
