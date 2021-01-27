package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.P4Parser;
import edu.hm.hafner.analysis.parser.PerlCriticParser;

/**
 * A Descriptor for the Perl Critic parser.
 *
 * @author Lorenz Munsch
 */
public class PerlCriticDescriptor extends ParserDescriptor {

    private static final String ID = "perl-critic";
    private static final String NAME = "Perl::Critic";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public PerlCriticDescriptor() {
        super(ID, NAME, new PerlCriticParser());
    }
}
