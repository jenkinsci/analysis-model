package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.IdeaInspectionParser;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;

/**
 * A descriptor for Infer. Delegates to {@link PmdParser}.
 *
 * @author Lorenz Munsch
 */
class InferDescriptor extends ParserDescriptor {
    private static final String ID = "infer";
    private static final String NAME = "IntelliJ IDEA Inspections";

    InferDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new PmdParser();
    }

    @Override
    public String getHelp() {
        return "Use option --pmd-xml.";
    }

    @Override
    public String getUrl() {
        return "http://fbinfer.com";
    }
}
