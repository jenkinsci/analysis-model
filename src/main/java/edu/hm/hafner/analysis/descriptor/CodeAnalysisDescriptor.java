package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.ClangTidyParser;
import edu.hm.hafner.analysis.parser.CodeAnalysisParser;

/**
 * A Descriptor for the Code Analysis parser.
 *
 * @author Lorenz Munsch
 */
public class CodeAnalysisDescriptor extends ParserDescriptor {

    private static final String ID = "code_analysis";
    private static final String NAME = "CodeAnalysis";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public CodeAnalysisDescriptor() {
        super(ID, NAME, new CodeAnalysisParser());
    }
}
