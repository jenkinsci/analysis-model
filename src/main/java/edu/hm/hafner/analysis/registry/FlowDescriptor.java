package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.FlowParser;

/**
 * A descriptor for Flow.
 *
 * @author Lorenz Munsch
 */
public class FlowDescriptor extends ParserDescriptor {
    private static final String ID = "flow";
    private static final String NAME = "Flow";

    public FlowDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new FlowParser();
    }

    @Override
    public String getUrl() {
        return "https://flow.org/";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/facebook/flow/main/website/static/img/logo.svg";
    }
}
