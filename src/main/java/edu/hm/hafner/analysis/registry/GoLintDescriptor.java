package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.GoLintParser;

/**
 * A Descriptor for the Go Lint parser.
 *
 * @author Lorenz Munsch
 */
class GoLintDescriptor extends ParserDescriptor {
    private static final String ID = "golint";
    private static final String NAME = "Go Lint";

    GoLintDescriptor() {
        super(ID, NAME, new GoLintParser());
    }
}
