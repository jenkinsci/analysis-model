package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GhsMultiParser;

/**
 * A descriptor for the Ghs Multi Compiler.
 *
 * @author Lorenz Munsch
 */
public class GhsMultiDescriptor extends ParserDescriptor {
    private static final String ID = "ghs-multi";
    private static final String NAME = "GHS Multi Compiler";

    public GhsMultiDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new GhsMultiParser();
    }
}
