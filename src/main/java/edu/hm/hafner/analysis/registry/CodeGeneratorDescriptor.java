package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CodeGeneratorParser;

/**
 * A descriptor for the Simulink Code Generator tool.
 *
 * @author Eva Habeeb
 */

class CodeGeneratorDescriptor extends ParserDescriptor {
    private static final String ID = "code-generator";
    private static final String NAME = "Code Generator Tool";

    CodeGeneratorDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CodeGeneratorParser();
    }

    @Override
    public String getUrl() {
        return "https://www.mathworks.com/help/stats/code-generation.html";
    }
}
