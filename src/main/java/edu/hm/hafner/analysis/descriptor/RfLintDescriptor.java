package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.QacSourceCodeAnalyserParser;
import edu.hm.hafner.analysis.parser.RfLintParser;

/**
 * A Descriptor for the Rf Lint parser.
 *
 * @author Lorenz Munsch
 */
public class RfLintDescriptor extends ParserDescriptor {

    private static final String ID = "rflint";
    private static final String NAME = "Robot Framework Lint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public RfLintDescriptor() {
        super(ID, NAME, new RfLintParser());
    }
}
