package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.GoVetParser;

/**
 * A Descriptor for the Go Lint parser.
 *
 * @author Lorenz Munsch
 */
class GoVetDescriptor extends ParserDescriptor {
    private static final String ID = "go-vet";
    private static final String NAME = "Go Vet";

    GoVetDescriptor() {
        super(ID, NAME);
    }

    @Override
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new GoVetParser();
    }
}
