package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.RobocopyParser;

/**
 * A Descriptor for the Robo Copy parser.
 *
 * @author Lorenz Munsch
 */
class RoboCopyDescriptor extends ParserDescriptor {
    private static final String ID = "robocopy";
    private static final String NAME = "Robocopy";

    RoboCopyDescriptor() {
        super(ID, NAME, new RobocopyParser());
    }
}
