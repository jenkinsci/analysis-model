package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.TaglistParser;
import edu.hm.hafner.analysis.parser.TiCcsParser;

/**
 * A Descriptor for the TiCcs parser.
 *
 * @author Lorenz Munsch
 */
public class TiCcsDescriptor extends ParserDescriptor {

    private static final String ID = "ti_ccs";
    private static final String NAME = "TiCcs";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public TiCcsDescriptor() {
        super(ID, NAME, new TiCcsParser());
    }
}
