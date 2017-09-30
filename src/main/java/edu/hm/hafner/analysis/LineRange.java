package edu.hm.hafner.analysis;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;

import edu.hm.hafner.util.Ensure;

/**
 * Defines a range of lines in a file. Lines start at 1.
 */
public class LineRange implements Iterable<Integer> {
    private final int from;
    private final int to;

    /**
     * Creates a new {@link LineRange} containing only one line.
     *
     * @param line the single line of this range
     */
    public LineRange(final int line) {
        this(line, line);
    }

    /**
     * Creates a new {@link LineRange}. The specified starting and ending lines are included in this range.
     *
     * @param from the first line of this range
     * @param to   the last line of this range
     */
    public LineRange(final int from, final int to) {
        Ensure.that(from > 0).isTrue("From must be a positive number: %d", from);
        Ensure.that(to > 0).isTrue("To must be a positive number: %d", to);

        if (from < to) {
            this.from = from;
            this.to = to;
        }
        else {
            this.from = to;
            this.to = from;
        }
    }

    /**
     * Returns the number of lines in this range.
     *
     * @return number of lines
     */
    public int size() {
        return to - from + 1;
    }

    /**
     * Returns the lines in this range in ascending order, starting with the smallest line.
     *
     * @return the lines, sorted
     */
    public int[] values() {
        int[] elements = new int[size()];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = from + i;
        }
        return elements;
    }

    @Override
    public String toString() {
        if (from == to) {
            return String.format("{%d}", from);
        }
        else if (from + 1 == to) {
            return String.format("{%d, %d}", from, to);
        }
        else {
            return String.format("{%d, ..., %d}", from, to);
        }
    }

    /**
     * Returns whether this range intersects with the specified range. Ranges intersect, if there is at least one line
     * that is part of both ranges.
     *
     * @param other the other line
     * @return {@code true} if this range intersects with the specified line, {@code false} otherwise
     */
    public boolean intersects(final LineRange other) {
        return from <= other.values()[other.size() - 1] && to >= other.values()[0];
    }

    /**
     * Returns whether this range contains the specified line.
     *
     * @param line the line to check
     * @return {@code true} if this range contains the specified line, {@code false} otherwise
     */
    public boolean contains(final int line) {
        return intersects(new LineRange(line));
    }

    /**
     * Returns whether this range contains all the lines of the specified range. I.e. this method checks
     * if the specified range is a sub-range of this range.
     *
     * @param range the line to check
     * @return {@code true} if this range contains all lines of the specified range, {@code false} otherwise
     */
    public boolean contains(final LineRange range) {
        for (int line : range) {
            if (!contains(line)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<Integer> iterator() {
        return Arrays.asList(ArrayUtils.toObject(values())).iterator();
    }
}
