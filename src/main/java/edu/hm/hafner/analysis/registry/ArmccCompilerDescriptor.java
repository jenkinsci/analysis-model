package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.Armcc52CompilerParser;
import edu.hm.hafner.analysis.parser.Armcc5CompilerParser;
import edu.hm.hafner.analysis.parser.ArmccCompilerParser;

import java.util.Collection;

/**
 * A descriptor for the Armcc compiler.
 *
 * @author Lorenz Munsch
 */
class ArmccCompilerDescriptor extends CompositeParserDescriptor {
    private static final String ID = "armcc";
    private static final String NAME = "Armcc Compiler";

    ArmccCompilerDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new ArmccCompilerParser(), new Armcc52CompilerParser(), new Armcc5CompilerParser());
    }
}
