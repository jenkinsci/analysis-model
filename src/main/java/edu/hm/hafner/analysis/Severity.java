package edu.hm.hafner.analysis;

import java.io.Serializable;

import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;

import edu.hm.hafner.util.Ensure;

/**
 * Severity of an issue. The predefined set of severities consists of an error and 3 warnings with {@link
 * Priority#HIGH}, {@link Priority#NORMAL}, or {@link Priority#LOW}. Additional severities can be created if this set of
 * severities is not sufficient. Note that new instances are not cached by this class so that there might exist several
 * severity instances with the same name.
 *
 * @author Ullrich Hafner
 */
public class Severity implements Serializable {
    private static final long serialVersionUID = 8921726169259131484L;

    /** An error, e.g. a compile error. */
    public static final Severity ERROR = new Severity("ERROR");
    /** A warning with {@link Priority#HIGH}. Mapping of warning priorities is determined by the corresponding tool. */
    public static final Severity WARNING_HIGH = new Severity("HIGH");
    /** A warning with {@link Priority#NORMAL}. Mapping of warning priorities is determined by the corresponding tool. */
    public static final Severity WARNING_NORMAL = new Severity("NORMAL");
    /** A warning with {@link Priority#LOW}. Mapping of warning priorities is determined by the corresponding tool. */
    public static final Severity WARNING_LOW = new Severity("LOW");
    
    private static final ImmutableSet<Severity> ALL_SEVERITIES 
            = Sets.immutable.of(ERROR, WARNING_HIGH, WARNING_NORMAL, WARNING_LOW);

    /**
     * Creates a new {@link Severity} that corresponds to the specified {@link Priority}.
     *
     * @param priority
     *         the priority
     *
     * @return the severity
     */
    public static Severity valueOf(final Priority priority) {
        if (priority == Priority.HIGH) {
            return WARNING_HIGH;
        }
        if (priority == Priority.NORMAL) {
            return WARNING_NORMAL;
        }
        if (priority == Priority.LOW) {
            return WARNING_LOW;
        }
        throw new NullPointerException("Priority must not be <<null>>.");
    }

    /**
     * Creates a new {@link Severity} with the specified name. If the name is the same as the name of one of the
     * predefined severities, then this existing severity is returned.
     *
     * @param name
     *         the name of the severity
     *
     * @return the severity
     */
    public static Severity valueOf(final String name) {
        if (ERROR.equalsIgnoreCase(name)) {
            return ERROR;
        }
        if (WARNING_HIGH.equalsIgnoreCase(name)) {
            return WARNING_HIGH;
        }
        if (WARNING_NORMAL.equalsIgnoreCase(name)) {
            return WARNING_NORMAL;
        }
        if (WARNING_LOW.equalsIgnoreCase(name)) {
            return WARNING_LOW;
        }
        return new Severity(name);
    }

    /**
     * Returns the set of predefined {@link Severity} instances.
     *
     * @return all predefined severities
     */
    public static ImmutableSet<Severity> getPredefinedValues() {
        return ALL_SEVERITIES;
    }

    private final String name;

    /**
     * Creates a new {@link Severity} with the specified name.
     *
     * @param name
     *         the name of the severity
     */
    public Severity(final String name) {
        Ensure.that(name).isNotBlank();

        this.name = name;
    }

    /**
     * Returns the name of the severity.
     *
     * @return the name of the severity
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Checks if this instance has a name that is equal to the specified name.
     *
     * @param severityName
     *         the name to check
     *
     * @return {@code true} if this instance has the same name, {@code false} otherwise
     */
    public boolean equalsIgnoreCase(final String severityName) {
        return getName().equalsIgnoreCase(severityName);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Severity severity = (Severity) o;

        return name.equals(severity.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
