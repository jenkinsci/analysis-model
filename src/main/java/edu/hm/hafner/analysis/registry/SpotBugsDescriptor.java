package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.Report.IssueType;

/**
 * A descriptor for SpotBugs.
 *
 * @author Lorenz Munsch
 */
class SpotBugsDescriptor extends FindBugsDescriptor {
    private static final String ID = "spotbugs";
    private static final String NAME = "SpotBugs";

    SpotBugsDescriptor() {
        super(ID, NAME);
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
        return "https://raw.githubusercontent.com/spotbugs/spotbugs.github.io/master/images/logos/spotbugs_icon_only_zoom_256px.png";
    }

    @Override
    public IssueType getType() {
        return IssueType.BUG;
    }
}
