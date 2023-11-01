package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.DrMemoryParser;

/**
 * A descriptor for the Dr. Memory errors.
 *
 * @author Lorenz Munsch
 */
public class DrMemoryDescriptor extends ParserDescriptor {
    private static final String ID = "dr-memory";
    private static final String NAME = "Dr. Memory";

    public DrMemoryDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new DrMemoryParser();
    }
}
