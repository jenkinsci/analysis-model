package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.PerlCriticParser;

/**
 * A Descriptor for the Perl Critic parser.
 *
 * @author Lorenz Munsch
 */
class PerlCriticDescriptor extends ParserDescriptor {

    private static final String ID = "perl-critic";
    private static final String NAME = "Perl::Critic";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    PerlCriticDescriptor() {
        super(ID, NAME, new PerlCriticParser());
    }
}
