package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.PreFastParser;
import edu.hm.hafner.analysis.parser.PuppetLintParser;

/**
 * A Descriptor for the Puppet Lint parser.
 *
 * @author Lorenz Munsch
 */
public class PuppetLintDescriptor extends ParserDescriptor {

    private static final String ID = "puppetlint";
    private static final String NAME = "Puppet-Lint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public PuppetLintDescriptor() {
        super(ID, NAME, new PuppetLintParser());
    }
}
