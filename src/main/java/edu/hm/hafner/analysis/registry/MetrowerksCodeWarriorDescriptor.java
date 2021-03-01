package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.MetrowerksCwCompilerParser;
import edu.hm.hafner.analysis.parser.MetrowerksCwLinkerParser;

/**
 * A descriptor for the Metrowerks CodeWarrior compiler.
 *
 * @author Lorenz Munsch
 */
class MetrowerksCodeWarriorDescriptor extends CompositeParserDescriptor {
    private static final String ID = "metrowerks";
    private static final String NAME = "Metrowerks CodeWarrior Compiler";

    MetrowerksCodeWarriorDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new MetrowerksCwCompilerParser(), new MetrowerksCwLinkerParser());
    }
}
