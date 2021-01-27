package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.AntJavacParser;

/**
 * A Descriptor for the AntJavac warnings.
 *
 * @author Lorenz Munsch
 */
public class AntJavacDescriptor extends ParserDescriptor {

    private static final String ID = "ant-javac";
    private static final String NAME = "Ant Javac";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public AntJavacDescriptor() {
        super(ID, NAME, new AntJavacParser());
    }
}
