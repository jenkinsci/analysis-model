package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.MentorParser;

/**
 * A descriptor for the Mentor Graphics Modelsim/Questa Simulators.
 *
 * @author Lorenz Munsch
 */
public class MentorGraphicsDescriptor extends ParserDescriptor {
    private static final String ID = "modelsim";
    private static final String NAME = "Mentor Graphics Modelsim/Questa Simulators";

    public MentorGraphicsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new MentorParser();
    }
}
