package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Defines the priority of an annotation.
 *
 * @author Ullrich Hafner
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
     * @return enumeration value.
     */
    public static Priority fromString(final String priority) {
        return Priority.valueOf(StringUtils.upperCase(priority));
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
     * @return {@code true} if the names are equal, {@code false} otherwise
     */
    public boolean equalsIgnoreCase(final String name) {
        return name().equalsIgnoreCase(name);
    }
}