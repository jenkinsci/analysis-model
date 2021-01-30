package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.SphinxBuildParser;

/**
 * A Descriptor for the Sphinx Build parser.
 *
 * @author Lorenz Munsch
 */
class SphinxBuildDescriptor extends ParserDescriptor {

    private static final String ID = "sphinx";
    private static final String NAME = "Sphinx-build";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    SphinxBuildDescriptor() {
        super(ID, NAME, new SphinxBuildParser());
    }
}
