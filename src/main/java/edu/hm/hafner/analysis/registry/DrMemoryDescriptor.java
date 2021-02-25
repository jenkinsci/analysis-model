package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.DrMemoryParser;

/**
 * A Descriptor for the Dr Memory parser.
 *
 * @author Lorenz Munsch
 */
class DrMemoryDescriptor extends ParserDescriptor {
    private static final String ID = "dr-memory";
    private static final String NAME = "Dr. Memory";

    DrMemoryDescriptor() {
        super(ID, NAME, new DrMemoryParser());
    }
}
