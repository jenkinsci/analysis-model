package lecture;

/**
 * A simple counter starting at 0 and counting by one.
 *
 * @author Ullrich Hafner
 */
public class OpenCounter implements Counter {
    private int count = 0;

    @Override
    public int getCount() {
        return count;
    }

    protected void setCount(final int count) {
        this.count = count;
    }

    @Override
    public void next() {
        count++;
    }

    /**
     * Resets the count to 0.
     */
    public void reset() {
        count = 0;
    }
}