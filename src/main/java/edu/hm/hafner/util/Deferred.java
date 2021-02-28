package edu.hm.hafner.util;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Deferred element wrapper of a singleton instance. Creates a constant instance when it is accessed the first time.
 *
 * @param <T>
 *         type of the instance
 */
public final class Deferred<T> {
    private volatile Supplier<T> supplier;

    private T object = null;

    /**
     * Creates a new instance that creates a singleton when it is accessed the first time.
     *
     * @param supplier
     *         the supplier to create the instance
     */
    public Deferred(final Supplier<T> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    /**
     * Returns the instance. If not yet created the instance will be created using the double checked locking idiom.
     *
     * @return the instance
     */
    public T get() {
        if (supplier != null) {
            synchronized (this) {
                if (supplier != null) {
                    object = supplier.get();
                    supplier = null;
                }
            }
        }
        return object;
    }
}
