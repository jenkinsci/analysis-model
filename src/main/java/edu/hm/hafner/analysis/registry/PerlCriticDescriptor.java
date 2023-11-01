package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PerlCriticParser;

/**
 * A descriptor for Perl::Critic.
 *
 * @author Lorenz Munsch
 */
public class PerlCriticDescriptor extends ParserDescriptor {
    private static final String ID = "perl-critic";
    private static final String NAME = "Perl::Critic";

    public PerlCriticDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new PerlCriticParser();
    }
}
