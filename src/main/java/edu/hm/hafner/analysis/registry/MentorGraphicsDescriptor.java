package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.MentorParser;

/**
 * A descriptor for the Mentor Graphics Modelsim/Questa Simulators.
 *
 * @author Lorenz Munsch
 */
class MentorGraphicsDescriptor extends ParserDescriptor {
    private static final String ID = "modelsim";
    private static final String NAME = "Mentor Graphics Modelsim/Questa Simulators";

    MentorGraphicsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new MentorParser();
    }
}
