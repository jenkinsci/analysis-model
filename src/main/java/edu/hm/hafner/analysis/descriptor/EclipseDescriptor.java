package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.EclipseMavenParser;
import edu.hm.hafner.analysis.parser.EclipseParser;

/**
 * A Descriptor for the Eclipse  parser.
 *
 * @author Lorenz Munsch
 */
public class EclipseDescriptor extends ParserDescriptor {

    private static final String ID = "eclipse";
    private static final String NAME = "Eclipse ECJ";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public EclipseDescriptor() {
        super(ID, NAME, new EclipseParser());
    }
}
