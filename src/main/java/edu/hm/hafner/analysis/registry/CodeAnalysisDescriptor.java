package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CodeAnalysisParser;

/**
 * A descriptor for the Code Analysis compiler.
 *
 * @author Lorenz Munsch
 */
public class CodeAnalysisDescriptor extends ParserDescriptor {
    private static final String ID = "code-analysis";
    private static final String NAME = "Code Analysis";

    public CodeAnalysisDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CodeAnalysisParser();
    }
}
