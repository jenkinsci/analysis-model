package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.ErlcParser;

/**
 * A Descriptor for the Erlc parser.
 *
 * @author Lorenz Munsch
 */
class ErlcDescriptor extends ParserDescriptor {
    private static final String ID = "erlc";
    private static final String NAME = "Erlang Compiler (erlc)";

    ErlcDescriptor() {
        super(ID, NAME, new ErlcParser());
    }
}
