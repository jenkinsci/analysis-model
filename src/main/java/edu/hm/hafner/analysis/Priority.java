package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Defines the priority of a warning. Not that issues are now categorized using a {@link Severity} which allows a
 * customization.
 *
 * @author Ullrich Hafner
 * @see Severity
 */
public enum Priority {
    /** High priority. */
    HIGH,
    /** Normal priority. */
    NORMAL,
    /** Low priority. */
    LOW;

    /**
     * Converts a String priority to an actual enumeration value.
     *
     * @param priority
     *         priority as a String
     *
     * @return enumeration value
     * @throws IllegalArgumentException
     *         if no priority with the specified name exists
     * @throws NullPointerException
     *         if {@code name} is {@code null}
     */
    public static Priority fromString(final String priority) {
        return Priority.valueOf(StringUtils.upperCase(priority));
    }

    /**
     * Converts a String priority to an actual enumeration value.
     *
     * @param priority
     *         priority as a String
     * @param defaultValue
     *         default priority, if the specified String is {@code null} or is not a valid {@link Priority} name
     *
     * @return enumeration value
     */
    public static Priority fromString(@CheckForNull final String priority, final Priority defaultValue) {
        if (priority == null || Arrays.stream(values()).map(Priority::name).noneMatch(name -> name.equals(priority))) {
            return defaultValue;
        }
        return fromString(priority);
    }

    /**
     * Gets the priorities starting from the specified priority to {@link Priority#HIGH}.
     *
     * @param minimumPriority
     *         the minimum priority
     *
     * @return the priorities starting from the specified priority
     */
    public static Collection<Priority> collectPrioritiesFrom(final Priority minimumPriority) {
        List<Priority> priorities = new ArrayList<>();
        priorities.add(Priority.HIGH);
        if (minimumPriority == Priority.NORMAL) {
            priorities.add(Priority.NORMAL);
        }
        else if (minimumPriority == Priority.LOW) {
            priorities.add(Priority.NORMAL);
            priorities.add(Priority.LOW);
        }
        return priorities;
    }

    /**
     * Compares the {@code name()} of this priority to the specified name, ignoring case considerations.
     *
     * @param name
     *         the name to compare with
     *
     * @return {@code true} if the names are equal, {@code false} otherwise
     */
    public boolean equalsIgnoreCase(final String name) {
        return name().equalsIgnoreCase(name);
    }
}