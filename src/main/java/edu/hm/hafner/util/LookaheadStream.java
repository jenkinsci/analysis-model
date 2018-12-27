package edu.hm.hafner.util;

import java.util.stream.Stream;

/**
 * FIXME: comment class.
 *
 * @author Ullrich Hafner
 */
public class LookaheadStream implements AutoCloseable {
    private Stream<String> stream;

    public LookaheadStream(final Stream<String> stream) {
        this.stream = stream;
    }

    @Override
    public void close() {
        stream.close();
    }
}
