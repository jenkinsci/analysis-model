package edu.hm.hafner.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A stream of lines with a lookahead of one line. Useful to parse a stream of lines when it is required to check if the
 * next line matches a given regular expression.
 *
 * @author Ullrich Hafner
 */
public class LookaheadStream implements AutoCloseable {
    private Stream<String> stream;
    private Iterator<String> lineIterator;
    private boolean isLookaheadFilled = false;
    private String lookaheadLine;

    /**
     * Wraps the specified stream of lines into a {@link LookaheadStream}.
     *
     * @param stream
     *         the lines to wrap
     */
    public LookaheadStream(final Stream<String> stream) {
        this.stream = stream;
        lineIterator = stream.iterator();
    }

    @Override
    public void close() {
        stream.close();
    }

    /**
     * Returns {@code true} if the stream has more elements. (In other words, returns {@code true} if {@link #next}
     * would return an element rather than throwing an exception.)
     *
     * @return {@code true} if the stream has more elements
     */
    public boolean hasNext() {
        return lineIterator.hasNext() || isLookaheadFilled;
    }

    /**
     * Returns the next element in the stream.
     *
     * @return the next element in the stream
     * @throws NoSuchElementException
     *         if the stream has no more elements
     */
    public String next() {
        if (isLookaheadFilled) {
            isLookaheadFilled = false;
            return lookaheadLine;
        }
        return lineIterator.next();
    }

    /**
     * Returns {@code true} if the stream has at least one more element that matches the given regular expression.
     *
     * @param regexp
     *         the regular expression
     *
     * @return {@code true} if the stream has more elements that match the regexp
     */
    public boolean hasNext(final String regexp) {
        if (!isLookaheadFilled) {
            if (!hasNext()) {
                return false;
            }
            lookaheadLine = next();
            isLookaheadFilled = true;
        }

        return Pattern.matches(regexp, lookaheadLine);
    }
}
