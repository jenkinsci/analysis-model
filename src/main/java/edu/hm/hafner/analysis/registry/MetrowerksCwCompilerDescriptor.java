package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.MetrowerksCwCompilerParser;

/**
 * A descriptor for the Metrowerks CodeWarrior compiler.
 *
 * @author Lorenz Munsch
 */
class MetrowerksCwCompilerDescriptor extends ParserDescriptor {
    private static final String ID = "metrowerks";
    private static final String NAME = "Metrowerks CodeWarrior Compiler";

    MetrowerksCwCompilerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new MetrowerksCwCompilerParser();
    }
}
