package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.SbtScalacParser;
import edu.hm.hafner.analysis.parser.ScalacParser;

/**
 * A descriptor for the  Scalac parser.
 *
 * @author Lorenz Munsch
 */
public class ScalaDescriptor extends CompositeParserDescriptor {
    private static final String ID = "scala";
    private static final String NAME = "Scala Compiler";

    public ScalaDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new ScalacParser(), new SbtScalacParser());
    }
}
