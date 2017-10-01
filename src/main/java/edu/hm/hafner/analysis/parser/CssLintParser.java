package edu.hm.hafner.analysis.parser;

/**
 * A parser for CSS-Lint checks warnings.
 *
 * @author Ulli Hafner
 */
public class CssLintParser extends LintParser {
    private static final long serialVersionUID = 8613418992526753095L;

    /**
     * Creates a new instance of {@link CssLintParser}.
     */
    public CssLintParser() {
        super("css-lint");
    }
}
