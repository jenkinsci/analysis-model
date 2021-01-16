package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.QacSourceCodeAnalyserParser;
import edu.hm.hafner.analysis.parser.RfLintParser;

/**
 * A Descriptor for the Rf Lint parser.
 *
 * @author Lorenz Munsch
 */
public class RfLintDescriptor extends ParserDescriptor {

    private static final String ID = "rf_lint";
    private static final String NAME = "RfLint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public RfLintDescriptor() {
        super(ID, NAME, new RfLintParser());
    }
}
