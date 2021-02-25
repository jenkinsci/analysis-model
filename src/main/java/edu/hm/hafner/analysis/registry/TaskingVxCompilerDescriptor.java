package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.TaskingVxCompilerParser;

/**
 * A Descriptor for the Tasking Vx Compiler parser.
 *
 * @author Lorenz Munsch
 */
class TaskingVxCompilerDescriptor extends ParserDescriptor {
    private static final String ID = "tasking-vx";
    private static final String NAME = "TASKING VX Compiler";

    TaskingVxCompilerDescriptor() {
        super(ID, NAME, new TaskingVxCompilerParser());
    }
}
