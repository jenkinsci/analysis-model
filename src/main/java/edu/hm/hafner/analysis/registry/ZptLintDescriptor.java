package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.ZptLintAdapter;

/**
 * A descriptor for the Yui Compressor parser.
 *
 * @author Lorenz Munsch
 */
public class ZptLintDescriptor extends ParserDescriptor {
    private static final String ID = "zptlint";
    private static final String NAME = "ZPT-Lint";

    public ZptLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new ZptLintAdapter();
    }
}
