package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.TaglistParser;
import edu.hm.hafner.analysis.parser.TiCcsParser;

/**
 * A Descriptor for the TiCcs parser.
 *
 * @author Lorenz Munsch
 */
public class TiCcsDescriptor extends ParserDescriptor {

    private static final String ID = "code-composer";
    private static final String NAME = "Texas Instruments Code Composer Studio";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public TiCcsDescriptor() {
        super(ID, NAME, new TiCcsParser());
    }
}
