package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PhpParser;

/**
 * A descriptor for PHP runtime errors and warnings.
 *
 * @author Lorenz Munsch
 */
public class PhpDescriptor extends ParserDescriptor {
    private static final String ID = "php";
    private static final String NAME = "PHP Runtime";

    public PhpDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new PhpParser();
    }
}
