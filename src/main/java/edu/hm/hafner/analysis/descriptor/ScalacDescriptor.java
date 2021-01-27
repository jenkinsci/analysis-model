package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.SbtScalacParser;
import edu.hm.hafner.analysis.parser.ScalacParser;

/**
 * A Descriptor for the  Scalac parser.
 *
 * @author Lorenz Munsch
 */
public class ScalacDescriptor extends ParserDescriptor {

    private static final String ID = "scalac";
    private static final String NAME = "Scala Compiler";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public ScalacDescriptor() {
        super(ID, NAME, new ScalacParser());
    }
}
