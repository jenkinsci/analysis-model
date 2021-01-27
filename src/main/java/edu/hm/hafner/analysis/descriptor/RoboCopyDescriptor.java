package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.RfLintParser;
import edu.hm.hafner.analysis.parser.RobocopyParser;

/**
 * A Descriptor for the Robo Copy parser.
 *
 * @author Lorenz Munsch
 */
public class RoboCopyDescriptor extends ParserDescriptor {

    private static final String ID = "robocopy";
    private static final String NAME = "Robocopy";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public RoboCopyDescriptor() {
        super(ID, NAME, new RobocopyParser());
    }
}
