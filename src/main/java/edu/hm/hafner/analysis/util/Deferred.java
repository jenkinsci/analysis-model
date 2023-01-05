package edu.hm.hafner.analysis.util;

import java.util.function.Supplier;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Deferred element wrapper of a singleton instance. Creates a constant instance when it is accessed the first time.
 *
 * @param <T>
 *         type of the instance
 */
public final class Deferred<T> {
    private final Supplier<T> supplier;

    @SuppressWarnings("PMD.AvoidUsingVolatile")
    @CheckForNull
    private volatile T object;

    /**
     * Creates a new instance that creates a singleton when it is accessed the first time.
     *
     * @param supplier
     *         the supplier to create the instance
     */
    public Deferred(final Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Returns the instance. If not yet created the instance will be created.
     *
     * @return the instance
     */
    public T get() {
        T tmp = object;
        if (tmp == null) {
            tmp = supplier.get();
        }
        object = tmp;
        return tmp;
    }
}
