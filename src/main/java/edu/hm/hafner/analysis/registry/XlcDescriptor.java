package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.XlcCompilerParser;
import edu.hm.hafner.analysis.parser.XlcLinkerParser;

/**
 * A descriptor for the IBM XLC Compiler.
 *
 * @author Lorenz Munsch
 */
class XlcDescriptor extends CompositeParserDescriptor {
    private static final String ID = "xlc";
    private static final String NAME = "IBM XLC Compiler";

    XlcDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new XlcCompilerParser(), new XlcLinkerParser());
    }
}
