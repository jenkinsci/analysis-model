package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.MetrowerksCwLinkerParser;
import edu.hm.hafner.analysis.parser.MsBuildParser;

/**
 * A Descriptor for the Ms Build parser.
 *
 * @author Lorenz Munsch
 */
public class MsBuildDescriptor extends ParserDescriptor {

    private static final String ID = "ms_build";
    private static final String NAME = "MsBuild";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public MsBuildDescriptor() {
        super(ID, NAME, new MsBuildParser());
    }
}
