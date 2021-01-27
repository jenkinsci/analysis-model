package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.CoolfluxChessccParser;
import edu.hm.hafner.analysis.parser.CppLintParser;

/**
 * A Descriptor for the Cpp Lint parser.
 *
 * @author Lorenz Munsch
 */
public class CppLintDescriptor extends ParserDescriptor {

    private static final String ID = "cpplint";
    private static final String NAME = "CppLint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public CppLintDescriptor() {
        super(ID, NAME, new CppLintParser());
    }
}
