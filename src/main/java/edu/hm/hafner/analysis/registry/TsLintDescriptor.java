package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A descriptor for TSLint. Delegates to {@link CheckStyleParser}.
 *
 * @author Lorenz Munsch
 */
class TsLintDescriptor extends ParserDescriptor {
    private static final String ID = "tslint";
    private static final String NAME = "TSLint  ";

    TsLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CheckStyleParser();
    }

    @Override
    public String getHelp() {
        return "Use option --format checkstyle.";
    }

    @Override
    public String getUrl() {
        return "https://palantir.github.io/tslint/";
    }
}
