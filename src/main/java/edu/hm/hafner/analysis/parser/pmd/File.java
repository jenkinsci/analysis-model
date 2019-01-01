package edu.hm.hafner.analysis.parser.pmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Java Bean class for a file of the PMD format.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public class File {
    /** Name of the file. */
    private String name;
    /** All violations of this file. */
    private final List<Violation> violations = new ArrayList<>();

    /**
     * Adds a new violation to this file.
     *
     * @param violation
     *            the new violation
     */
    public void addViolation(final Violation violation) {
        violations.add(violation);
    }

    /**
     * Returns all violations of this file. The returned collection is
     * read-only.
     *
     * @return all violations in this file
     */
    public Collection<Violation> getViolations() {
        return Collections.unmodifiableCollection(violations);
    }

    /**
     * Returns the name of this file.
     *
     * @return the name of this file
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this file to the specified value.
     *
     * @param name the value to set
     */
    public void setName(final String name) {
        this.name = name;
    }
}

