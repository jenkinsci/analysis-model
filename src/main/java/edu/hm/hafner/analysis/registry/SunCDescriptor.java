package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.SunCParser;

/**
 * A Descriptor for the Sun C parser.
 *
 * @author Lorenz Munsch
 */
class SunCDescriptor extends ParserDescriptor {
    private static final String ID = "sunc";
    private static final String NAME = "SUN C++ Compiler";

    SunCDescriptor() {
        super(ID, NAME, new SunCParser());
    }
}
