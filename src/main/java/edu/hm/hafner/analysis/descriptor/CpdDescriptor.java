package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.YuiCompressorParser;
import edu.hm.hafner.analysis.parser.dry.cpd.CpdParser;

/**
 * A Descriptor for the Cpfr parser.
 *
 * @author Lorenz Munsch
 */
public class CpdDescriptor extends ParserDescriptor {

    private static final String ID = "cpd";
    private static final String NAME = "Cpd";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public CpdDescriptor() {
        super(ID, NAME, new CpdParser());
    }
}
