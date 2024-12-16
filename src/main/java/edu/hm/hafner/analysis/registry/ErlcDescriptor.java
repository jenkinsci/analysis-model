package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ErlcParser;

/**
 * A descriptor for the Erlang Compiler (erlc).
 *
 * @author Lorenz Munsch
 */
class ErlcDescriptor extends ParserDescriptor {
    private static final String ID = "erlc";
    private static final String NAME = "Erlang Compiler (erlc)";

    ErlcDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new ErlcParser();
    }
}
