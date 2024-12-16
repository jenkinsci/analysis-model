package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CoolfluxChessccParser;

/**
 * A descriptor for the Coolflux DSP Compiler.
 *
 * @author Lorenz Munsch
 */
class CoolfluxChessccDescriptor extends ParserDescriptor {
    private static final String ID = "coolflux";
    private static final String NAME = "Coolflux DSP Compiler";

    CoolfluxChessccDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CoolfluxChessccParser();
    }
}
