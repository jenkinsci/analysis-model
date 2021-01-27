package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.IarParser;
import edu.hm.hafner.analysis.parser.IdeaInspectionParser;

/**
 * A Descriptor for the Idea Inspection parser.
 *
 * @author Lorenz Munsch
 */
public class IdeaInspectionDescriptor extends ParserDescriptor {

    private static final String ID = "idea";
    private static final String NAME = "IntelliJ IDEA Inspections";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public IdeaInspectionDescriptor() {
        super(ID, NAME, new IdeaInspectionParser());
    }
}
