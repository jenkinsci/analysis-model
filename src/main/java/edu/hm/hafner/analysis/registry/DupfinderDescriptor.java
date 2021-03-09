package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.dry.dupfinder.DupFinderParser;

import static edu.hm.hafner.analysis.registry.CpdDescriptor.*;

/**
 * A descriptor for Resharper DupFinder.
 *
 * @author Lorenz Munsch
 */
class DupfinderDescriptor extends ParserDescriptor {
    private static final String ID = "dupfinder";
    private static final String NAME = "Resharper DupFinder";

    DupfinderDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new DupFinderParser();
    }

    @Override
    public String getDescription(final Issue issue) {
        return getDuplicateCode(issue.getAdditionalProperties());
    }
}
