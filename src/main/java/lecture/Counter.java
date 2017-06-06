package lecture;

/**
 * A counter.
 *
 * @author Ullrich Hafner
 */
public interface Counter {
    /**
     * Returns the current count.
     *
     * @return the count
     */
    int getCount();

    /**
     * Increments the count by one.
     */
    void next();
}
