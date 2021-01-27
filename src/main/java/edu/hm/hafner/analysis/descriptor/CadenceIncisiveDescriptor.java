package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.CadenceIncisiveParser;

/**
 * A Descriptor for the CadenceIncisive warnings.
 *
 * @author Lorenz Munsch
 */
public class CadenceIncisiveDescriptor extends ParserDescriptor {

    private static final String ID = "cadence";
    private static final String NAME = "Cadence Incisive";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public CadenceIncisiveDescriptor() {
        super(ID, NAME, new CadenceIncisiveParser());
    }
    }
