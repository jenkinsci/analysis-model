package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.dry.dupfinder.DupFinderParser;
import edu.hm.hafner.analysis.parser.dry.simian.SimianParser;

/**
 * A Descriptor for the Simian parser.
 *
 * @author Lorenz Munsch
 */
public class SimianDescriptor extends ParserDescriptor {

    private static final String ID = "simian";
    private static final String NAME = "Simian";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public SimianDescriptor() {
        super(ID, NAME, new SimianParser());
    }
}
