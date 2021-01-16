package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.PhpParser;
import edu.hm.hafner.analysis.parser.PreFastParser;

/**
 * A Descriptor for the Pre Fast parser.
 *
 * @author Lorenz Munsch
 */
public class PreFastDescriptor extends ParserDescriptor {

    private static final String ID = "pre_fast";
    private static final String NAME = "PreFast";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public PreFastDescriptor() {
        super(ID, NAME, new PreFastParser());
    }
}
