package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.findbugs.FindBugsMessages;
import edu.hm.hafner.analysis.parser.findbugs.FindBugsParser;
import edu.hm.hafner.analysis.parser.findbugs.FindBugsParser.PriorityProperty;
import edu.hm.hafner.util.Deferred;

/**
 * A descriptor for SpotBugs.
 *
 * @author Lorenz Munsch
 */
class SpotBugsDescriptor extends ParserDescriptor {
    private static final String ID = "spotbugs";
    private static final String NAME = "SpotBugs";

    private final Deferred<FindBugsMessages> messages = new Deferred<>(FindBugsMessages::new);

    SpotBugsDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new FindBugsParser(PriorityProperty.RANK);
    }

    @Override
    public String getPattern() {
        return "**/spotbugsXml.xml";
    }

    @Override
    public String getUrl() {
        return "https://spotbugs.github.io";
    }

    @Override
    public String getIconUrl() {
        return "https://github.com/spotbugs/spotbugs.github.io/blob/master/images/logos/spotbugs_icon_only_zoom_256px.png";
    }

    @Override
    public String getDescription(final Issue issue) {
        return messages.get().getMessage(issue.getType());
    }
}
