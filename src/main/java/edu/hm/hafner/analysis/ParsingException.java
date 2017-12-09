package edu.hm.hafner.analysis;

/**
 * Indicates that during parsing a non recoverable error has been occurred.
 *
 * @author Ullrich Hafner
 */
public class ParsingException extends RuntimeException {
    private static final long serialVersionUID = -9016364685084958944L;

    /**
     * Constructs a new {@link ParsingException} with the specified cause.
     *
     * @param cause
     *         the cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public ParsingException(final Throwable cause) {
        super("Exception occurred during parsing:\n" + cause.getMessage(), cause);
    }

    /**
     * Constructs a new {@link ParsingException} with the specified cause and message.
     *
     * @param cause
     *         the cause (which is saved for later retrieval by the {@link #getCause()} method).
     * @param fileName
     *         the file that could not be parsed
     */
    public ParsingException(final Throwable cause, final String fileName) {
        super(String.format("Exception occurred during parsing of file '%s'.%n%s", fileName, cause.getMessage()), cause);
    }
}
