package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.IdeaInspectionParser;

/**
 * A descriptor for the IntelliJ IDEA Inspections.
 *
 * @author Lorenz Munsch
 */
public class IdeaInspectionDescriptor extends ParserDescriptor {
    private static final String ID = "idea";
    private static final String NAME = "IntelliJ IDEA Inspections";

    public IdeaInspectionDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new IdeaInspectionParser();
    }

    @Override
    public String getUrl() {
        return "https://www.jetbrains.com/help/idea/code-inspection.html";
    }
}
