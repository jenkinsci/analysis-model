package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.SphinxBuildParser;

/**
 * A Descriptor for the Sphinx Build parser.
 *
 * @author Lorenz Munsch
 */
class SphinxBuildDescriptor extends ParserDescriptor {
    private static final String ID = "sphinx";
    private static final String NAME = "Sphinx-build";

    SphinxBuildDescriptor() {
        super(ID, NAME, new SphinxBuildParser());
    }
}
