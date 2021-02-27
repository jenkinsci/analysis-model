package edu.hm.hafner.analysis.registry;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.IssueParser;

/**
 * Interface to describe all descriptors.
 *
 * @author Lorenz Munsch
 */
public abstract class ParserDescriptor {
    private final String id;
    private final String name;

    /**
     * Creates a new {@link ParserDescriptor} instance.
     *
     * @param id
     *         the technical ID
     * @param name
     *         the name of the parser
     */
    ParserDescriptor(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the technical ID of the parser.
     *
     * @return the technical id of the parser
     */
    public String getId() {
        return id;
    }

    /**
     * Returns a human readable name of the parser. Note that this property is not yet localizable.
     *
     * @return the human readable name
     */
    public String getName() {
        return name;
    }

    /**
     * Creates a new {@link IssueParser} instance.
     *
     * @return the parser
     */
    public abstract IssueParser createParser();

    /**
     * Returns the default filename pattern for this tool. Override if your parser typically works on a specific file.
     * This pattern will be interpreted as an ANT pattern or glob, resp.
     *
     * @return the default pattern
     */
    public String getPattern() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns an optional help text that can provide useful hints on how to configure the static analysis tool so
     * that the report files could be parsed by Jenkins. This help can be a plain text message or an HTML snippet.
     *
     * @return the help
     */
    public String getHelp() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns an optional URL to the homepage of the static analysis tool.
     *
     * @return the help
     */
    public String getUrl() {
        return StringUtils.EMPTY;
    }
}
