package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.TaglistParser;
import edu.hm.hafner.analysis.parser.TaskingVxCompilerParser;

/**
 * A Descriptor for the Tasking Vx Compiler parser.
 *
 * @author Lorenz Munsch
 */
public class TaskingVxCompilerDescriptor extends ParserDescriptor {

    private static final String ID = "tasking-vx";
    private static final String NAME = "TASKING VX Compiler";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public TaskingVxCompilerDescriptor() {
        super(ID, NAME, new TaskingVxCompilerParser());
    }
}
