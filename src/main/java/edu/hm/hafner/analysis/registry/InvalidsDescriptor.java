package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.InvalidsParser;

/**
 * A descriptor for the Invalids parser.
 *
 * @author Lorenz Munsch
 */
public class InvalidsDescriptor extends ParserDescriptor {
    private static final String ID = "invalids";
    private static final String NAME = "Oracle Invalids";

    public InvalidsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new InvalidsParser();
    }
}
