package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.TaskingVxCompilerParser;

/**
 * A descriptor for the Tasking Vx Compiler parser.
 *
 * @author Lorenz Munsch
 */
public class TaskingVxCompilerDescriptor extends ParserDescriptor {
    private static final String ID = "tasking-vx";
    private static final String NAME = "TASKING VX Compiler";

    public TaskingVxCompilerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new TaskingVxCompilerParser();
    }
}
