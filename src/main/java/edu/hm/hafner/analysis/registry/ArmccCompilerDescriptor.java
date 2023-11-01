package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.Armcc5CompilerParser;
import edu.hm.hafner.analysis.parser.ArmccCompilerParser;

/**
 * A descriptor for the Armcc compiler.
 *
 * @author Lorenz Munsch
 */
public class ArmccCompilerDescriptor extends CompositeParserDescriptor {
    private static final String ID = "armcc";
    private static final String NAME = "Armcc Compiler";

    public ArmccCompilerDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new ArmccCompilerParser(), new Armcc5CompilerParser());
    }
}
