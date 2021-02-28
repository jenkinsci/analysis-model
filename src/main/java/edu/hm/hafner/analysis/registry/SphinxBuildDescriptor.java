package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SphinxBuildParser;

/**
 * A descriptor for Sphinx build warnings.
 *
 * @author Lorenz Munsch
 */
class SphinxBuildDescriptor extends ParserDescriptor {
    private static final String ID = "sphinx";
    private static final String NAME = "Sphinx Build";

    SphinxBuildDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new SphinxBuildParser();
    }
}
