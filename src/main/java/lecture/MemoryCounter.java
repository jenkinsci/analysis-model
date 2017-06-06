package lecture;

/**
 * A simple counter that can store and recall its count.
 *
 * @author Ullrich Hafner
 */
public class MemoryCounter extends OpenCounter {
    private int memory = 0;

    /**
     * Stores the current count.
     */
    public void store() {
        memory = getCount();
    }

    /**
     * Recalls the count to the stored value.
     */
    public void recall() {
        setCount(memory);
    }
}
