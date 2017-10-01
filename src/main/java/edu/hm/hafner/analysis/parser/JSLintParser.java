package edu.hm.hafner.analysis.parser;

/**
 * A parser for JSLint checks warnings.
 *
 * @author Gavin Mogan <gavin@kodekoan.com>
 */
public class JSLintParser extends LintParser {
    private static final long serialVersionUID = 8613418992526753095L;

    /**
     * Creates a new instance of {@link JSLintParser}.
     */
    public JSLintParser() {
        super("js-lint");
    }
}
