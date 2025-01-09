package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.FindBugsMessages;
import edu.hm.hafner.analysis.parser.FindBugsParser;
import edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty;
import edu.hm.hafner.analysis.util.Deferred;

/**
 * A descriptor for the FindBugs parser.
 *
 * @author Lorenz Munsch
 */
class FindBugsDescriptor extends ParserDescriptor {
    private static final String ID = "findbugs";
    private static final String NAME = "FindBugs";

    /** Key to define minimum number of duplicate lines for high priority warnings. */
    public static final String PRIORITY_OPTION_KEY = "SPOT_BUGS_CONFIDENCE";

    private final Deferred<FindBugsMessages> messages = new Deferred<>(FindBugsMessages::new);

    FindBugsDescriptor() {
        super(ID, NAME);
    }

    FindBugsDescriptor(final String id, final String name) {
        super(id, name);
    }

    @Override
    public IssueParser create(final Option... options) {
        for (Option option : options) {
            if (PRIORITY_OPTION_KEY.equals(option.getKey())
                    && PriorityProperty.CONFIDENCE.name().equals(option.getValue())) {
                return new FindBugsParser(PriorityProperty.CONFIDENCE);
            }
        }
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

    @Override
    public IssueType getType() {
        return IssueType.BUG;
    }
}
