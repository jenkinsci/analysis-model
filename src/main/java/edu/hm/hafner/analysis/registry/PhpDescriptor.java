package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.PhpParser;

/**
 * A Descriptor for the Php parser.
 *
 * @author Lorenz Munsch
 */
class PhpDescriptor extends ParserDescriptor {
    private static final String ID = "php";
    private static final String NAME = "PHP Runtime";

    PhpDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new PhpParser();
    }
}
