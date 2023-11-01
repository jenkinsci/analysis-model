package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CoolfluxChessccParser;

/**
 * A descriptor for the Coolflux DSP Compiler.
 *
 * @author Lorenz Munsch
 */
public class CoolfluxChessccDescriptor extends ParserDescriptor {
    private static final String ID = "coolflux";
    private static final String NAME = "Coolflux DSP Compiler";

    public CoolfluxChessccDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CoolfluxChessccParser();
    }
}
