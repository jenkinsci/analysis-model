package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PerlCriticParser;

/**
 * A descriptor for Perl::Critic.
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
    public IssueParser create(final Option... options) {
        return new PerlCriticParser();
    }
}
