package lecture;

import java.util.NoSuchElementException;

/**
 * A simple gift box.
 *
 * @author Ullrich Hafner
 */
public class GiftBox implements Box {
    private Object object;

    public boolean isEmpty() {
        return object == null;
    }

    @Override
    public Object get() throws NoSuchElementException {
        if (object == null) {
            throw new NoSuchElementException("Die Box ist leer");
        }
        return object;
    }

    @Override
    public void put(final Object object) {
        if (this.object != null) {
            throw new IllegalStateException("Die Box ist nicht leer");
        }
        this.object = object;
    }
}
