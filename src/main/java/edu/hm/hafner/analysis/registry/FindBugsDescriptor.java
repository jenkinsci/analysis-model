package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.findbugs.FindBugsMessages;
import edu.hm.hafner.analysis.parser.findbugs.FindBugsParser;
import edu.hm.hafner.analysis.parser.findbugs.FindBugsParser.PriorityProperty;
import edu.hm.hafner.util.Deferred;

/**
 * A descriptor for the Find Bugs parser.
 *
 * @author Lorenz Munsch
 */
class FindBugsDescriptor extends ParserDescriptor {
    private static final String ID = "findbugs";
    private static final String NAME = "FindBugs";

    private final Deferred<FindBugsMessages> messages = new Deferred<>(FindBugsMessages::new);

    FindBugsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new FindBugsParser(PriorityProperty.RANK);
    }

    @Override
    public String getPattern() {
        return "**/findbugsXml.xml";
    }

    @Override
    public String getDescription(final Issue issue) {
        return messages.get().getMessage(issue.getType());
    }
}
