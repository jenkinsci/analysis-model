package edu.hm.hafner.analysis.registry;

import java.util.AbstractMap.SimpleImmutableEntry;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
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
     * Returns a human-readable name of the parser. Note that this property is not yet localizable.
     *
     * @return the human-readable name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the parser. The type is used to customize parsers in the UI.
     * This default implementation returns {@link Type#WARNING}.
     * Override this method if your parser is of a different type.
     *
     * @return the type of the parser
     */
    public Type getType() {
        return Type.WARNING;
    }

    /**
     * Creates a new {@link IssueParser} instance.
     *
     * @param options
     *         options to configure the parser - may customize the new parser instance (if supported by the selected
     *         tool)
     *
     * @return the parser
     */
    public abstract IssueParser createParser(Option... options);

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
     * Returns an optional help text that can provide useful hints on how to configure the static analysis tool so that
     * the report files could be parsed by Jenkins. This help can be a plain text message or an HTML snippet.
     *
     * @return the help
     */
    public String getHelp() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns whether an optional help text is available for this parser.
     *
     * @return {@code true} if there is a help text available
     * @see #getHelp()
     */
    public final boolean hasHelp() {
        return StringUtils.isNotBlank(getHelp());
    }

    /**
     * Returns an optional URL to the homepage of the static analysis tool.
     *
     * @return the help
     */
    public String getUrl() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns whether the URL for the parser is set.
     *
     * @return {@code true} if there is a URL available
     * @see #getUrl()
     */
    public final boolean hasUrl() {
        return StringUtils.isNotBlank(getUrl());
    }

    /**
     * Returns an optional URL to the icon or logo of the static analysis tool.
     *
     * @return the help
     */
    public String getIconUrl() {
        return StringUtils.EMPTY;
    }

    /**
     * Returns a detailed description of the specified issue. If there is no additional description is available, then
     * an empty String is returned.
     *
     * @param issue
     *         the issue to get the description for
     *
     * @return the description
     */
    public String getDescription(final Issue issue) {
        return issue.getDescription();
    }

    /**
     * Returns the type of the parser. The type is used to customize parsers in the UI.
     */
    public enum Type {
        /** A parser that scans the output of a build tool to find warnings. */
        WARNING,
        /** A parser that scans the output of a build tool to find bugs. */
        BUG,
        /** A parser that scans the output of a build tool to find vulnerabilities. */
        VULNERABILITY,
        /** A parser that scans the output of a build tool to find vulnerabilities. */
        DUPLICATION
    }

    /**
     * A parser configuration option. Basically an immutable key and value pair.
     */
    public static class Option extends SimpleImmutableEntry<String, String> {
        private static final long serialVersionUID = 7376822311558465523L;

        /**
         * Creates an entry representing a mapping from the specified key to the specified value.
         *
         * @param key
         *         the key represented by this entry
         * @param value
         *         the value represented by this entry
         */
        public Option(final String key, final String value) {
            super(key, value);
        }
    }
}
