package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.RobocopyParser;
import edu.hm.hafner.analysis.parser.SbtScalacParser;

/**
 * A Descriptor for the Sbt Scalac parser.
 *
 * @author Lorenz Munsch
 */
public class SbtScalacDescriptor extends ParserDescriptor {

    private static final String ID = "sbt-scalac";
    private static final String NAME = "SbtScalac";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public SbtScalacDescriptor() {
        super(ID, NAME, new SbtScalacParser());
    }
}
