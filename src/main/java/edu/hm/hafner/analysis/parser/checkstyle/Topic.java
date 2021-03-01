package edu.hm.hafner.analysis.parser.checkstyle;

import org.apache.commons.lang3.StringUtils;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Java Bean class representing a DocBook subsection.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("PMD.DataClass")
public class Topic {
    @CheckForNull
    private String name;
    @CheckForNull
    private String value;

    /**
     * Returns the name of this topic.
     *
     * @return the name
     */
    public String getName() {
        return StringUtils.defaultString(name);
    }

    /**
     * Sets the name of this topic.
     *
     * @param name
     *         the name
     */
    public void setName(@CheckForNull final String name) {
        this.name = name;
    }

    /**
     * Returns the value of this topic.
     *
     * @return the value
     */
    public String getValue() {
        return StringUtils.defaultString(value);
    }

    /**
     * Sets the value of this topic.
     *
     * @param value
     *         the value
     */
    public void setValue(@CheckForNull final String value) {
        this.value = value;
    }
}
