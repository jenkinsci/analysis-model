package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;

/**
 * A descriptor for Infer. Delegates to {@link PmdParser}.
 *
 * @author Lorenz Munsch
 */
class InferDescriptor extends ParserDescriptor {
    private static final String ID = "infer";
    private static final String NAME = "Infer";

    InferDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new PmdParser();
    }

    @Override
    public String getHelp() {
        return "Use option --pmd-xml.";
    }

    @Override
    public String getUrl() {
        return "https://fbinfer.com";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/facebook/infer/main/website/static/img/logo.png";
    }
}
