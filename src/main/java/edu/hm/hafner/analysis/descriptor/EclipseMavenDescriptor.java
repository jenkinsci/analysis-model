package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.DrMemoryParser;
import edu.hm.hafner.analysis.parser.EclipseMavenParser;

/**
 * A Descriptor for the Eclipse Maven parser.
 *
 * @author Lorenz Munsch
 */
public class EclipseMavenDescriptor extends ParserDescriptor {

    private static final String ID = "eclipse-maven";
    private static final String NAME = "Eclipse Maven";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public EclipseMavenDescriptor() {
        super(ID, NAME, new EclipseMavenParser());
    }
}
