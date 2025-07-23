package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CheckStyleParser;
import edu.hm.hafner.analysis.parser.EsLintParser;

import java.util.Collection;
import java.util.List;

/**
 * A descriptor for ESLint. Supports json format {@link EsLintParser} and for backward compatibility checkstyle {@link CheckStyleParser}.
 *
 * @author Lorenz Munsch
 */
class EsLintDescriptor extends CompositeParserDescriptor {
    private static final String ID = "eslint";
    private static final String NAME = "ESLint";

    EsLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return List.of(new CheckStyleParser(), new EsLintParser());
    }

    @Override
    public String getHelp() {
        return "Use option <code>--format json</code>, <code>--format json-with-metadata</code> or <code>--format checkstyle</code>.";
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
