package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.XlcCompilerParser;

/**
 * A descriptor for the IBM XLC Compiler.
 *
 * @author Lorenz Munsch
 */
class XlcCompilerDescriptor extends ParserDescriptor {
    private static final String ID = "xlc";
    private static final String NAME = "IBM XLC Compiler";

    XlcCompilerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new XlcCompilerParser();
    }
}
