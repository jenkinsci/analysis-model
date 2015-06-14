package edu.hm.hafner.sokoban;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

import edu.hm.hafner.util.Point;

/**
 * A collection of points that contains no duplicates. More formally, a {@link PointSet} contains no pair of elements
 * {@code p1} and {@code p2} such that {@code p1.isEqualTo(p2)}. The order of the added elements is preserved.
 *
 * @author Ullrich Hafner
 */
public class PointSet implements Iterable<Point> {
    private final Set<Point> elements;

    /**
     * Creates a copy of the specified set of points.
     *
     * @param copy the copy to create
     */
    public PointSet(final PointSet copy) {
        elements = new HashSet<>(copy.elements);
    }

    /**
     * Creates an empty set of points.
     */
    public PointSet() {
        elements = new HashSet<>();
    }

    /**
     * Creates a set of points.
     *
     * @param initialElements the initial elements of this set
     */
    public PointSet(final Point... initialElements) {
        elements = Sets.newHashSet(initialElements);
    }

    /**
     * Creates a set of points.
     *
     * @param initialElements the initial elements of this set
     */
    public PointSet(final Iterable<Point> initialElements) {
        elements = Sets.newHashSet(initialElements);
    }

    /**
     * Adds the specified point to the tail of this set. If this set already contains a point at the same coordinates
     * then the set will not be changed.
     *
     * @param value the point to add
     * @return {@code true} if the point has been added, {@code false} if the point was already part of this set
     */
    public boolean add(final Point value) {
        return elements.add(value);
    }

    /**
     * Removes the specified point from this set. If this set does not contain a point at the same
     * coordinates then the set will not be changed.
     *
     * @param value the point to remove
     * @return {@code true} if the point has been removed, {@code false} if the point was not part of this set
     */
    public boolean remove(final Point value) {
       return elements.remove(value);
    }

    /**
     * Returns the size of this set.
     *
     * @return the number of points in this set
     */
    public int size() {
        return elements.size();
    }

    /**
     * Returns whether this set already contains a point that is equal to the specified point. More formally, returns
     * {@code true} if this set already contains an elements {@code p} such that {@code p.isEqualTo(point)}.
     *
     * @param point the point to check for
     * @return {@code true} if this list contains a point that is equal to the specified point
     */
    public boolean contains(final Point point) {
        return elements.contains(point);
    }

    @Override
    public String toString() {
        return elements.toString();
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PointSet points = (PointSet)o;

        return elements.equals(points.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    @Override
    public Iterator<Point> iterator() {
        return elements.iterator();
    }

    /**
     * Clears the contents of this set to the specified elements.
     *
     * @param elements the new elements
     */
    public void setElements(final PointSet elements) {
        this.elements.clear();
        this.elements.addAll(elements.elements);
    }
}
