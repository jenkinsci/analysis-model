package edu.hm.hafner.util;

import java.util.Formatter;

import com.google.errorprone.annotations.FormatMethod;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Thrown by various accessor methods to indicate that the element being requested does not exist. Enhances the
 * exception {@link java.util.NoSuchElementException} by providing a constructor with format string.
 *
 * @author Ullrich Hafner
 */
@SuppressFBWarnings("NM")
public class NoSuchElementException extends java.util.NoSuchElementException {
    private static final long serialVersionUID = -355717274596010159L;

    /**
     * Constructs a {@code NoSuchElementException}, saving a reference to the error message for later retrieval by the
     * {@link #getMessage()} method.
     *
     * @param messageFormat
     *         A format string as described in <a href="../util/Formatter.html#syntax">Format string syntax</a>
     * @param args
     *         Arguments referenced by the format specifiers in the format string.  If there are more arguments than
     *         format specifiers, the extra arguments are ignored.  The number of arguments is variable and may be zero.
     *         The maximum number of arguments is limited by the maximum dimension of a Java array as defined by
     *         <cite>The Java&trade; Virtual Machine Specification</cite>. The behaviour on a {@code null} argument
     *         depends on the <a href="../util/Formatter.html#syntax">conversion</a>.
     *
     * @see Formatter
     */
    @FormatMethod
    public NoSuchElementException(final String messageFormat, final Object... args) {
        super(String.format(messageFormat, args));
    }
}
