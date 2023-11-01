package edu.hm.hafner.analysis.registry;

/**
 * A descriptor for SpotBugs.
 *
 * @author Lorenz Munsch
 */
public class SpotBugsDescriptor extends FindBugsDescriptor {
    private static final String ID = "spotbugs";
    private static final String NAME = "SpotBugs";

    public SpotBugsDescriptor() {
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
}
