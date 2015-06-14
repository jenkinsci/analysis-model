package edu.hm.hafner.sokoban;

import javax.annotation.concurrent.Immutable;

/**
 * A point represents a location in {@code (x,y)} coordinate space,
 * specified in integer precision. Instances of this class are immutable.
 *
 * @author Ullrich Hafner
 */
@Immutable
public class Point {
    private final int x;
    private final int y;
    private final String display;

    /**
     * Creates a new instance of {@code Point}.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
        display = String.format("(%d, %d)", x, y);
    }

    /**
     * Returns the x coordinate of this point.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this point.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the point left of this point.
     *
     * @return the point to the left
     */
    public Point moveLeft() {
        return new Point(x - 1, y);
    }

    /**
     * Returns the point right of this point.
     *
     * @return the point to the right
     */
    public Point moveRight() {
        return new Point(x + 1, y);
    }

    /**
     * Returns the point above this point.
     *
     * @return the point above
     */
    public Point moveUp() {
        return new Point(x, y - 1);
    }

    /**
     * Returns the point below of this point.
     *
     * @return the point below
     */
    public Point moveDown() {
        return new Point(x, y + 1);
    }

    @Override
    public String toString() {
        return display;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point point = (Point)o;

        if (x != point.x) {
            return false;
        }
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
