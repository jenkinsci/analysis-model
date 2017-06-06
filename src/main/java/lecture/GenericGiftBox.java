package lecture;

import java.util.NoSuchElementException;

/**
 * A simple gift box.
 *
 * @author Ullrich Hafner
 * @param <T> the type of the elements in this box
 */
public class GenericGiftBox<T> {
    private T object;

    public boolean isEmpty() {
        return object == null;
    }

    public T get() throws NoSuchElementException {
        if (object == null) {
            throw new NoSuchElementException("Die Box ist leer");
        }
        return object;
    }

    public void put(final T object) {
        if (this.object != null) {
            throw new IllegalStateException("Die Box ist nicht leer");
        }
        this.object = object;
    }
}
