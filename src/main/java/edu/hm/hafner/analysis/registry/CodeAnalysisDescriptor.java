package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.CodeAnalysisParser;

/**
 * A Descriptor for the Code Analysis parser.
 *
 * @author Lorenz Munsch
 */
class CodeAnalysisDescriptor extends ParserDescriptor {
    private static final String ID = "code-analysis";
    private static final String NAME = "Code Analysis";

    CodeAnalysisDescriptor() {
        super(ID, NAME, new CodeAnalysisParser());
    }
}
