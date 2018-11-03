package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Parses a file and returns the issues reported in this file.
 *
 * @author Ullrich Hafner
 */
public abstract class IssueParser implements Serializable {
    private static final long serialVersionUID = 200992696185460268L;

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
    public abstract Report parse(Path file, Charset charset, Function<String, String> preProcessor)
            throws ParsingException, ParsingCanceledException;
    
    /**
     * Returns whether this parser accepts the specified file as valid input. Parsers may reject a file if it is
     * in the wrong format and will produce exceptions otherwise.
     *
     * @param file
     *         the file to parse
     * @param charset
     *         the encoding to use when reading files
     *
     * @return {@code true} if this parser accepts this file as valid input, or {@code false} if the file could not be
     *         parsed by this parser
     */
    public boolean accepts(final Path file, final Charset charset) {
        return true;
    }

    /**
     * Parses the specified file for issues. Parses a file without pre-processing the lines.
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
    public Report parse(final Path file, final Charset charset) throws ParsingException, ParsingCanceledException {
        return parse(file, charset, Function.identity());
    }

    /**
     * Returns whether the specified file is an XML file. This method just checks if the first 10 lines contain the
     * XML tag rather than parsing the whole document.
     * 
     * @param file the file to check
     * @return {@code true} if the file is an XML file, {@code false} otherwise
     */
    protected boolean isXmlFile(final Path file) {
        try (Stream<String> lines = Files.lines(file)) {
            return lines.limit(10).anyMatch(line -> line.contains("<?xml"));
        }
        catch (IOException | InvalidPathException ignored) {
            return false;
        }
    }
}

