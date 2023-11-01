package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.Gcc4CompilerParser;
import edu.hm.hafner.analysis.parser.Gcc4LinkerParser;

/**
 * A descriptor for the GNU C Compiler (gcc).
 *
 * @author Lorenz Munsch
 */
public class Gcc4Descriptor extends CompositeParserDescriptor {
    private static final String ID = "gcc";
    private static final String NAME = "GNU C Compiler (gcc)";

    public Gcc4Descriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new Gcc4CompilerParser(), new Gcc4LinkerParser());
    }
}
