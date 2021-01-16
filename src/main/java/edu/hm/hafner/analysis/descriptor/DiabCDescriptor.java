package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.CppLintParser;
import edu.hm.hafner.analysis.parser.DiabCParser;

/**
 * A Descriptor for the Diab C parser.
 *
 * @author Lorenz Munsch
 */
public class DiabCDescriptor extends ParserDescriptor {

    private static final String ID = "diab_c";
    private static final String NAME = "DiabC";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public DiabCDescriptor() {
        super(ID, NAME, new DiabCParser());
    }
}
