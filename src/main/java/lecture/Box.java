package lecture;

import java.util.NoSuchElementException;

/**
 * A gift box.
 *
 * @author Ullrich Hafner
 */
public interface Box {
    /**
     * Returns whether this box is empty or not.
     *
     * @return {@code true} if this box is empty, {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * Returns the gift in this box.
     *
     * @return the gift
     * @throws NoSuchElementException if the box is empty
     * @see #isEmpty()
     */
    Object get() throws NoSuchElementException;

    /**
     * Puts a gift into this box.
     *
     * @param object the gift to put into the box
     * @throws IllegalStateException if the box is not empty
     * @see #isEmpty()
     */
    void put(Object object) throws IllegalStateException;
}
