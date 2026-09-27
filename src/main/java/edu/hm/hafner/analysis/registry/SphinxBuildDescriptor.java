package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SphinxBuildLinkCheckParser;
import edu.hm.hafner.analysis.parser.SphinxBuildParser;
import java.util.Collection;

/**
 * A descriptor for Sphinx build warnings.
 *
 * @author Lorenz Munsch
 */
class SphinxBuildDescriptor extends CompositeParserDescriptor {
    private static final String ID = "sphinx";
    private static final String NAME = "Sphinx Build";

    SphinxBuildDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new SphinxBuildParser(), new SphinxBuildLinkCheckParser());
    }
}
