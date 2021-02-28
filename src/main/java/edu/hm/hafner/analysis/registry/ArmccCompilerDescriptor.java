package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ArmccCompilerParser;

/**
 * A descriptor for the Armcc compiler.
 *
 * @author Lorenz Munsch
 */
class ArmccCompilerDescriptor extends ParserDescriptor {
    private static final String ID = "armcc";
    private static final String NAME = "Armcc Compiler";

    ArmccCompilerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new ArmccCompilerParser();
    }
}
