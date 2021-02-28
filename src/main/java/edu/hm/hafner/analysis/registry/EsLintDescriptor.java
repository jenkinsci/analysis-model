package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ErrorProneParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A descriptor for for ESlint. Delegates to {@link CheckStyleParser}.
 *
 * @author Lorenz Munsch
 */
class EsLintDescriptor extends ParserDescriptor {
    private static final String ID = "eslint";
    private static final String NAME = "ESlint";

    EsLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new CheckStyleParser();
    }

    @Override
    public String getHelp() {
        return "Use option --format checkstyle.";
    }

    @Override
    public String getUrl() {
        return "https://eslint.org";
    }
}
