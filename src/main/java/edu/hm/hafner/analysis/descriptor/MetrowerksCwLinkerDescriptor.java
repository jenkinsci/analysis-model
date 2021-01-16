package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.MetrowerksCwCompilerParser;
import edu.hm.hafner.analysis.parser.MetrowerksCwLinkerParser;

/**
 * A Descriptor for the Metrowerks Cw Linker parser.
 *
 * @author Lorenz Munsch
 */
public class MetrowerksCwLinkerDescriptor extends ParserDescriptor {

    private static final String ID = "metrowerks_cw_linker";
    private static final String NAME = "MetrowerksCwLinker";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public MetrowerksCwLinkerDescriptor() {
        super(ID, NAME, new MetrowerksCwLinkerParser());
    }
}
