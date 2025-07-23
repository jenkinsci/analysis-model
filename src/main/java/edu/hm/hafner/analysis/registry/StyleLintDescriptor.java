package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CheckStyleParser;
import edu.hm.hafner.analysis.parser.StyleLintParser;

import java.util.Collection;
import java.util.List;

/**
 * A descriptor for Stylelint. Supports json format {@link StyleLintParser} and for backward compatibility checkstyle {@link CheckStyleParser}.
 *
 * @author Alexander Brandes
 */
class StyleLintDescriptor extends CompositeParserDescriptor {
    private static final String ID = "stylelint";
    private static final String NAME = "Stylelint";

    StyleLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return List.of(new CheckStyleParser(), new StyleLintParser());
    }

    @Override
    public String getHelp() {
        return "<p>Use <code>--formatter json</code></p>"
                + "<p>For checkstyle format install <a href='https://www.npmjs.com/package/stylelint-checkstyle-reporter'>stylelint-checkstyle-reporter</a>."
                + "<br/>Use <code>--custom-formatter node_modules/stylelint-checkstyle-reporter/index.js -o stylelint-warnings.xml</code>"
                + "<br/>The checkstyle is deprecated. Use the json formatter instead."
                + "</p>";
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
