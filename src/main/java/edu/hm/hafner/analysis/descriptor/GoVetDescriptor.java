package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.GoLintParser;
import edu.hm.hafner.analysis.parser.GoVetParser;

/**
 * A Descriptor for the Go Lint parser.
 *
 * @author Lorenz Munsch
 */
public class GoVetDescriptor extends ParserDescriptor {

    private static final String ID = "go_vet";
    private static final String NAME = "GoVet";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public GoVetDescriptor() {
        super(ID, NAME, new GoVetParser());
    }
}
