package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.GoVetParser;

/**
 * A descriptor for the Go Vet.
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
    public IssueParser create(final Option... options) {
        return new GoVetParser();
    }
}
