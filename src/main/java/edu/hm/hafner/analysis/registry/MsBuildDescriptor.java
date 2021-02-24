package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.MsBuildParser;

/**
 * A Descriptor for the Ms Build parser.
 *
 * @author Lorenz Munsch
 */
class MsBuildDescriptor extends ParserDescriptor {

    private static final String ID = "msbuild";
    private static final String NAME = "MSBuild";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    MsBuildDescriptor() {
        super(ID, NAME, new MsBuildParser());
    }
}
