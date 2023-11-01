package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.RobocopyParser;

/**
 * A descriptor for the Robocopy.
 *
 * @author Lorenz Munsch
 */
public class RoboCopyDescriptor extends ParserDescriptor {
    private static final String ID = "robocopy";
    private static final String NAME = "Robocopy";

    public RoboCopyDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new RobocopyParser();
    }
}
