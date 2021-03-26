package edu.hm.hafner.analysis.parser.checkstyle;

import org.apache.commons.lang3.StringUtils;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Java Bean class representing a Checkstyle rule.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("PMD.DataClass")
public class Rule {
    /** Description to indicate that the rules stored in this plug-in don't match with the generators version. */
    static final String UNDEFINED_DESCRIPTION = StringUtils.EMPTY;
    /** The name of the subsection that defines a description in the docbook files. */
    private static final String DESCRIPTION_SUBSECTION_NAME = "Description";

    @CheckForNull
    private String name;
    @CheckForNull
    private String description;

    /**
     * Instantiates a new rule.
     */
    public Rule() {
        // nothing to do
    }

    /**
     * Instantiates a new rule.
     *
     * @param name
     *         the name of the rule
     */
    public Rule(@CheckForNull final String name) {
        this.name = name;
        description = UNDEFINED_DESCRIPTION;
    }

    /**
     * Returns the name of this rule.
     *
     * @return the name
     */
    public String getName() {
        return StringUtils.defaultString(name);
    }

    /**
     * Sets the name of this rule.
     *
     * @param name
     *         the name
     */
    public void setName(@CheckForNull final String name) {
        this.name = name;
    }

    /**
     * Returns the description of this rule.
     *
     * @return the description
     */
    public String getDescription() {
        return StringUtils.defaultString(description);
    }

    /**
     * Sets the description of this rule. The description is only set if the topic is a description.
     *
     * @param topic
     *         the topic that might contain the description
     */
    @SuppressFBWarnings("IMPROPER_UNICODE")
    public void setDescription(final Topic topic) {
        if (DESCRIPTION_SUBSECTION_NAME.equalsIgnoreCase(topic.getName())) {
            description = topic.getValue();
        }
    }
}
