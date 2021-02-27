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

    PerlCriticDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new PerlCriticParser();
    }
}
