package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.StyleCopParser;
import edu.hm.hafner.analysis.parser.SunCParser;

/**
 * A Descriptor for the Sun C parser.
 *
 * @author Lorenz Munsch
 */
public class SunCDescriptor extends ParserDescriptor {

    private static final String ID = "sun_c";
    private static final String NAME = "SunC";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public SunCDescriptor() {
        super(ID, NAME, new SunCParser());
    }
}
