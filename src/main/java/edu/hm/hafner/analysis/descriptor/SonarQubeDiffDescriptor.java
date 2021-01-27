package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.ScalacParser;
import edu.hm.hafner.analysis.parser.SonarQubeDiffParser;

/**
 * A Descriptor for the Sonar Qube Diff parser.
 *
 * @author Lorenz Munsch
 */
public class SonarQubeDiffDescriptor extends ParserDescriptor {

    private static final String ID = "sonar_diff";
    private static final String NAME = "SonarQubeDiff";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public SonarQubeDiffDescriptor() {
        super(ID, NAME, new SonarQubeDiffParser());
    }
}
