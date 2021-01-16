package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.DrMemoryParser;
import edu.hm.hafner.analysis.parser.EclipseMavenParser;

/**
 * A Descriptor for the Eclipse Maven parser.
 *
 * @author Lorenz Munsch
 */
public class EclipseMavenDescriptor extends ParserDescriptor {

    private static final String ID = "eclipse_maven";
    private static final String NAME = "EclipseMaven";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public EclipseMavenDescriptor() {
        super(ID, NAME, new EclipseMavenParser());
    }
}
