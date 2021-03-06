package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A descriptor for ktlint. Delegates to {@link CheckStyleParser}.
 *
 * @author Lorenz Munsch
 */
class KtLintDescriptor extends ParserDescriptor {
    private static final String ID = "ktlint";
    private static final String NAME = "KtLint";

    KtLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CheckStyleParser();
    }

    @Override
    public String getHelp() {
        return "Use option --reporter=checkstyle.";
    }

    @Override
    public String getUrl() {
        return "https://ktlint.github.io";
    }
}
