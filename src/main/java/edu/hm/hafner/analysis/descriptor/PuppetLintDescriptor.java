package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.PuppetLintParser;

/**
 * A Descriptor for the Puppet Lint parser.
 *
 * @author Lorenz Munsch
 */
class PuppetLintDescriptor extends ParserDescriptor {

    private static final String ID = "puppetlint";
    private static final String NAME = "Puppet-Lint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    PuppetLintDescriptor() {
        super(ID, NAME, new PuppetLintParser());
    }
}
