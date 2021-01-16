package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.CodeAnalysisParser;
import edu.hm.hafner.analysis.parser.CoolfluxChessccParser;

/**
 * A Descriptor for the Coold Flux Chesscc parser.
 *
 * @author Lorenz Munsch
 */
public class CoolfluxChessccDescriptor extends ParserDescriptor {

    private static final String ID = "cool_flux_chesscc";
    private static final String NAME = "CoolFluxChesscc";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public CoolfluxChessccDescriptor() {
        super(ID, NAME, new CoolfluxChessccParser());
    }
}
