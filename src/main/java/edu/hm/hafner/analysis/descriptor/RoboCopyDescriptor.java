package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.RobocopyParser;

/**
 * A Descriptor for the Robo Copy parser.
 *
 * @author Lorenz Munsch
 */
class RoboCopyDescriptor extends ParserDescriptor {

    private static final String ID = "robocopy";
    private static final String NAME = "Robocopy";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    RoboCopyDescriptor() {
        super(ID, NAME, new RobocopyParser());
    }
}
