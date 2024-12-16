package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.YamlLintAdapter;

/**
 * A descriptor for YamlLint.
 *
 * @author Lorenz Munsch
 */
class YamlLintDescriptor extends ParserDescriptor {
    private static final String ID = "yamllint";
    private static final String NAME = "YamlLint";

    YamlLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new YamlLintAdapter();
    }

    @Override
    public String getUrl() {
        return "https://yamllint.readthedocs.io/";
    }

    @Override
    public String getHelp() {
        return "Use option -f parsable.";
    }
}
