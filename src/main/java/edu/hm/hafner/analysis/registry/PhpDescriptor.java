package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PhpParser;

/**
 * A descriptor for PHP runtime errors and warnings.
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
    public IssueParser create(final Option... options) {
        return new PhpParser();
    }
}
