package edu.hm.hafner.analysis;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.function.Function;

import edu.hm.hafner.analysis.parser.MavenConsoleParser;

/**
 * Parses a file and returns the issues reported in this file.
 *
 * @author Ullrich Hafner
 */
public abstract class IssueParser implements Serializable {
    private static final long serialVersionUID = 200992696185460268L;

    /**
     * References an issue that has an affected file that is the file that has been parsed. E.g., the {@link
     * MavenConsoleParser} creates issues from an input file that has references to itself. There are no other affected
     * files involved.
     */
    public static final String SELF = "<SELF>";

    /**
     * Parses the specified file for issues.
     *
     * @param file
     *         the file to parse
     * @param charset
     *         the encoding to use when reading files
     * @param preProcessor
     *         pre processes each input line before handing it to the actual parser
     *
     * @return the issues
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the parsing has been aborted by the user
     */
    public abstract Issues parse(File file, Charset charset, Function<String, String> preProcessor)
            throws ParsingException, ParsingCanceledException;

    /**
     * Parses the specified file for issues.
     *
     * @param file
     *         the file to parse
     * @param charset
     *         the encoding to use when reading files
     *
     * @return the issues
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the parsing has been aborted by the user
     */
    public Issues parse(final File file, final Charset charset) throws ParsingException, ParsingCanceledException {
        return parse(file, charset, Function.identity());
    }
}

