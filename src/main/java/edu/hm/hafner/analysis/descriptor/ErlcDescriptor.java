package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.EclipseXMLParser;
import edu.hm.hafner.analysis.parser.ErlcParser;

/**
 * A Descriptor for the Erlc parser.
 *
 * @author Lorenz Munsch
 */
public class ErlcDescriptor extends ParserDescriptor {

    private static final String ID = "erlc";
    private static final String NAME = "Erlc";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public ErlcDescriptor() {
        super(ID, NAME, new ErlcParser());
    }
}
