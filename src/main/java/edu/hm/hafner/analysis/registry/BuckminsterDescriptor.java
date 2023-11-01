package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.BuckminsterParser;

/**
 * A descriptor for the Buckminster compiler.
 *
 * @author Lorenz Munsch
 */
public class BuckminsterDescriptor extends ParserDescriptor {
    private static final String ID = "buckminster";
    private static final String NAME = "Buckminster";

    public BuckminsterDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new BuckminsterParser();
    }
}
