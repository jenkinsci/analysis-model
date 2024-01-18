package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.BanditAdapter;

/**
 * A descriptor for the Bandit.
 *
 * @author Ullrich Hafner
 */
class BanditDescriptor extends ParserDescriptor {
    private static final String ID = "bandit";
    private static final String NAME = "Bandit";

    BanditDescriptor() {
        super(ID, NAME);
    }

    @Override
    public String getUrl() {
        return "https://github.com/PyCQA/bandit";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/PyCQA/bandit/main/logo/logo.svg";
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new BanditAdapter();
    }
}
