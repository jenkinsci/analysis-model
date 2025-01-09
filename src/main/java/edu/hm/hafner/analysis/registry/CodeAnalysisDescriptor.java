package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CodeAnalysisParser;

/**
 * A descriptor for the Code Analysis compiler.
 *
 * @author Lorenz Munsch
 */
class CodeAnalysisDescriptor extends ParserDescriptor {
    private static final String ID = "code-analysis";
    private static final String NAME = "Code Analysis";

    CodeAnalysisDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CodeAnalysisParser();
    }
}
