package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.fxcop.FxCopParser;

/**
 * A descriptor for FxCop.
 *
 * @author Lorenz Munsch
 */
public class FxcopDescriptor extends ParserDescriptor {
    private static final String ID = "fxcop";
    private static final String NAME = "FxCop";

    public FxcopDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new FxCopParser();
    }
}
