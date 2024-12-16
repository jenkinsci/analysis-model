package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A descriptor for Stylelint. Delegates to {@link CheckStyleParser}.
 *
 * @author Alexander Brandes
 */
class StyleLintDescriptor extends ParserDescriptor {
    private static final String ID = "stylelint";
    private static final String NAME = "Stylelint";

    StyleLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CheckStyleParser();
    }

    @Override
    public String getHelp() {
        return "Requires <a href='https://www.npmjs.com/package/stylelint-checkstyle-reporter'>stylelint-checkstyle-reporter</a>."
                + "<br/>Use <code>--custom-formatter node_modules/stylelint-checkstyle-reporter/index.js -o stylelint-warnings.xml</code>";
    }

    @Override
    public String getUrl() {
        return "https://stylelint.io/";
    }

    @Override
    public String getIconUrl() {
        return "https://cdn.worldvectorlogo.com/logos/stylelint.svg";
    }
}
