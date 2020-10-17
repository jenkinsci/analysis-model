package edu.hm.hafner.analysis;

import java.io.Serializable;
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
     * @param readerFactory
     *         provides a reader to the reports
     *
     * @return the issues
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the parsing has been aborted by the user
     */
    public abstract Report parse(ReaderFactory readerFactory)
            throws ParsingException, ParsingCanceledException;

    /**
     * Parses the specified file for issues. Invokes the parser using {@link #parse(ReaderFactory)} and sets the file
     * name of the report.
     *
     * @param readerFactory
     *         provides a reader to the reports
     *
     * @return the issues
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the parsing has been aborted by the user
     */
    public Report parseFile(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        Report report = parse(readerFactory);
        report.addFileName(readerFactory.getFileName());
        return report;
    }

    /**
     * Returns whether this parser accepts the specified file as valid input. Parsers may reject a file if it is in the
     * wrong format to avoid exceptions during parsing.
     *
     * @param readerFactory
     *         provides a reader to the reports
     *
     * @return {@code true} if this parser accepts this file as valid input, or {@code false} if the file could not be
     *         parsed by this parser
     */
    public boolean accepts(final ReaderFactory readerFactory) {
        return true;
    }

    /**
     * Returns whether the specified file is an XML file. This method just checks if the first 10 lines contain the XML
     * tag rather than parsing the whole document.
     *
     * @param readerFactory
     *         the file to check
     *
     * @return {@code true} if the file is an XML file, {@code false} otherwise
     */
    protected boolean isXmlFile(final ReaderFactory readerFactory) {
        try (Stream<String> lines = readerFactory.readStream()) {
            return lines.limit(10).anyMatch(line -> line.contains("<?xml"));
        }
        catch (ParsingException ignored) {
            return false;
        }
    }
}

