package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.PuppetLintParser;

/**
 * A Descriptor for the Puppet Lint parser.
 *
 * @author Lorenz Munsch
 */
class PuppetLintDescriptor extends ParserDescriptor {
    private static final String ID = "puppetlint";
    private static final String NAME = "Puppet-Lint";

    PuppetLintDescriptor() {
        super(ID, NAME, new PuppetLintParser());
    }
}
