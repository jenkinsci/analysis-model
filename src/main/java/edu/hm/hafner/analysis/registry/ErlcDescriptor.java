package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ErlcParser;

/**
 * A descriptor for the Erlang Compiler (erlc).
 *
 * @author Lorenz Munsch
 */
public class ErlcDescriptor extends ParserDescriptor {
    private static final String ID = "erlc";
    private static final String NAME = "Erlang Compiler (erlc)";

    public ErlcDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new ErlcParser();
    }
}
