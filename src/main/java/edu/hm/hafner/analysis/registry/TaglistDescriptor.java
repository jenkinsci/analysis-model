package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.TaglistParser;

/**
 * A descriptor for the Taglist Maven Plugin.
 *
 * @author Lorenz Munsch
 */
class TaglistDescriptor extends ParserDescriptor {
    private static final String ID = "taglist";
    private static final String NAME = "Maven Taglist Plugin";

    TaglistDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new TaglistParser();
    }

    @Override
    public String getPattern() {
        return "**/taglist.xml";
    }

    @Override
    public String getUrl() {
        return "https://www.mojohaus.org/taglist-maven-plugin";
    }
}
