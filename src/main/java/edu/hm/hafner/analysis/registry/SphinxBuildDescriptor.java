package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SphinxBuildLinkCheckParser;
import edu.hm.hafner.analysis.parser.SphinxBuildParser;

/**
 * A descriptor for Sphinx build warnings.
 *
 * @author Lorenz Munsch
 */
public class SphinxBuildDescriptor extends CompositeParserDescriptor {
    private static final String ID = "sphinx";
    private static final String NAME = "Sphinx Build";

    public SphinxBuildDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new SphinxBuildParser(), new SphinxBuildLinkCheckParser());
    }
}
