package edu.hm.hafner.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * A stream of lines with a lookahead of one line. Useful to parse a stream of lines when it is required to check if the
 * next line matches a given regular expression.
 *
 * @author Ullrich Hafner
 */
public class LookaheadStream implements AutoCloseable {
    private final Stream<String> stream;
    private final Iterator<String> lineIterator;
    private final String fileName;

    private boolean isLookaheadFilled = false;
    private String lookaheadLine = StringUtils.EMPTY;
    private int line = 0;

    /**
     * Wraps the specified stream of lines into a {@link LookaheadStream}.
     *
     * @param stream
     *         the lines to wrap
     */
    public LookaheadStream(final Stream<String> stream) {
        this(stream, StringUtils.EMPTY);
    }

    /**
     * Wraps the specified stream of lines into a {@link LookaheadStream}.
     *
     * @param stream
     *         the lines to wrap
     * @param fileName
     *         the file name of the stream
     */
    public LookaheadStream(final Stream<String> stream, final String fileName) {
        this.stream = stream;
        lineIterator = stream.iterator();
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
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
            fillLookahead();
        }

        return Pattern.compile(regexp).matcher(lookaheadLine).find();
    }

    /**
     * Peeks the next element in the stream. I.e., the next element is returned but not removed from the stream so that
     * the next call of {@link #next()} will again return this value.
     *
     * @return the next element in the stream
     * @throws NoSuchElementException
     *         if the stream has no more elements
     */
    public String peekNext() {
        if (!isLookaheadFilled) {
            fillLookahead();
        }
        return lookaheadLine;
    }

    private void fillLookahead() {
        lookaheadLine = lineIterator.next();
        isLookaheadFilled = true;
    }

    /**
     * Returns the next element in the stream.
     *
     * @return the next element in the stream
     * @throws NoSuchElementException
     *         if the stream has no more elements
     */
    public String next() {
        line++;

        if (isLookaheadFilled) {
            isLookaheadFilled = false;
            return lookaheadLine;
        }
        return lineIterator.next();
    }

    /**
     * Returns the line number of the line that has been handed out using the {@link #next()} method.
     *
     * @return the current line, or 0 if no line has been handed out yet
     */
    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("[%d] -> '%s'", line, lookaheadLine);
    }
}
