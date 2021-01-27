package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.MavenConsoleParser;
import edu.hm.hafner.analysis.parser.MetrowerksCwCompilerParser;

/**
 * A Descriptor for the Metrowerks Cw Comnpiler parser.
 *
 * @author Lorenz Munsch
 */
public class MetrowerksCwCompilerDescriptor extends ParserDescriptor {

    private static final String ID = "metrowerks";
    private static final String NAME = "Metrowerks CodeWarrior";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public MetrowerksCwCompilerDescriptor() {
        super(ID, NAME, new MetrowerksCwCompilerParser());
    }
}
