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
public class MetrowerksCodeWarriorDescriptor extends CompositeParserDescriptor {
    private static final String ID = "metrowerks";
    private static final String NAME = "Metrowerks CodeWarrior Compiler";

    public MetrowerksCodeWarriorDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new MetrowerksCwCompilerParser(), new MetrowerksCwLinkerParser());
    }

    @Override
    public String getHelp() {
        return "<p><p>Ensure that the output from the CodeWarrior build tools is in the expected format. "
                + "If there are warnings present, but they are not found, then it is likely that the format is incorrect. "
                + "The mwccarm compiler and mwldarm linker tools may support a configurable message style. "
                + "This can be used to enforce the expected output format, which may be different from Metrowerks "
                + "CodeWarrior (and thus require a different tool). For example the following could be appended to "
                + "the build flags:</p>"
                + "<p><code>-msgstyle gcc -nowraplines</code></p></p>";
    }
}
