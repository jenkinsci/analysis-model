package edu.hm.hafner.analysis.parser.checkstyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Java Bean class for a file of the Checkstyle format.
 *
 * @author Ullrich Hafner
 */
public class File {
    /** Name of the file. */
    @CheckForNull
    private String name;
    /** All errors of this file. */
    private final List<Error> errors = new ArrayList<>();

    /**
     * Adds a new violation to this file.
     *
     * @param violation
     *            the new violation
     */
    public void addError(final Error violation) {
        errors.add(violation);
    }

    /**
     * Returns all violations of this file. The returned collection is
     * read-only.
     *
     * @return all violations in this file
     */
    public Collection<Error> getErrors() {
        return Collections.unmodifiableCollection(errors);
    }

    /**
     * Returns the name of this file.
     *
     * @return the name of this file
     */
    @CheckForNull
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this file to the specified value.
     *
     * @param name the value to set
     */
    public void setName(@CheckForNull final String name) {
        this.name = name;
    }
}
