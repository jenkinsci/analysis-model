package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.SonarQubeIssuesParser;
import edu.hm.hafner.analysis.parser.SphinxBuildParser;

/**
 * A Descriptor for the Sphinx Build parser.
 *
 * @author Lorenz Munsch
 */
public class SphinxBuildDescriptor extends ParserDescriptor {

    private static final String ID = "sphinx";
    private static final String NAME = "Sphinx-build";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public SphinxBuildDescriptor() {
        super(ID, NAME, new SphinxBuildParser());
    }
}
