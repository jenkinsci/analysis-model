package edu.hm.hafner.analysis.parser.dry.dupfinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Java Bean class for a Reshaper DupFinder duplicate.
 *
 * @author Rafal Jasica
 */
public class Duplicate {
    /** The duplicated cost. */
    private int cost;

    /** All files of this duplication. */
    private final List<Fragment> fragments = new ArrayList<>();

    /**
     * Returns the duplicate cost.
     *
     * @return the duplicate cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets the duplicate cost to the specified value.
     *
     * @param cost the value to set
     */
    public void setCost(final int cost) {
        this.cost = cost;
    }

    /**
     * Adds a new file to this duplication.
     *
     * @param file
     *            the new file
     */
    public void addFragment(final Fragment file) {
        fragments.add(file);
    }

    /**
     * Returns all files of the duplication. The returned collection is
     * read-only.
     *
     * @return all files
     */
    public Collection<Fragment> getFragments() {
        return Collections.unmodifiableCollection(fragments);
    }
}

