package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A descriptor for ESLint. Delegates to {@link CheckStyleParser}.
 *
 * @author Lorenz Munsch
 */
public class EsLintDescriptor extends ParserDescriptor {
    private static final String ID = "eslint";
    private static final String NAME = "ESLint";

    public EsLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
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

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/eslint/eslint/main/docs/src/static/icon.svg";
    }
}
