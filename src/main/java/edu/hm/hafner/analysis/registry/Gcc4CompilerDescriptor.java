package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.Gcc4CompilerParser;

/**
 * A descriptor for the GNU C Compiler (gcc).
 *
 * @author Lorenz Munsch
 */
class Gcc4CompilerDescriptor extends ParserDescriptor {
    private static final String ID = "gcc";
    private static final String NAME = "GNU C Compiler (gcc)";

    Gcc4CompilerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new Gcc4CompilerParser();
    }
}
