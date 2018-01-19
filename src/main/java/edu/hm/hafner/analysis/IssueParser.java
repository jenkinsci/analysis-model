package edu.hm.hafner.analysis;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * Parses a file and returns the issues reported in this file.
 *
 * @author Ulli Hafner
 */
public abstract class IssueParser implements Serializable {
    /**
     * Returns the issues found in the specified file.
     *
     * @param file
     *         the file to parse
     * @param charset
     *         the encoding to use when reading files
     * @param builder
     *         the issue builder to use
     *
     * @return the issues found
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the parsing has been aborted by the user
     */
    public abstract Issues<Issue> parse(File file, Charset charset, IssueBuilder builder)
            throws ParsingException, ParsingCanceledException;
}

