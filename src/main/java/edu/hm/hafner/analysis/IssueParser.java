package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.Locale;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.SecureXmlParserFactory;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Parses a file and returns the issues reported in this file.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("checkstyle:JavadocVariable")
public abstract class IssueParser implements Serializable {
    private static final long serialVersionUID = 200992696185460268L;

    protected static final String ADDITIONAL_PROPERTIES = "additionalProperties";
    protected static final String CATEGORY = "category";
    protected static final String COLUMN_END = "columnEnd";
    protected static final String COLUMN_START = "columnStart";
    protected static final String DESCRIPTION = "description";
    protected static final String FILE_NAME = "fileName";
    protected static final String FINGERPRINT = "fingerprint";
    protected static final String ID = "id";
    protected static final String LINE_END = "lineEnd";
    protected static final String LINE_RANGES = "lineRanges";
    protected static final String LINE_RANGE_END = "end";
    protected static final String LINE_RANGE_START = "start";
    protected static final String LINE_START = "lineStart";
    protected static final String MESSAGE = "message";
    protected static final String MODULE_NAME = "moduleName";
    protected static final String ORIGIN = "origin";
    protected static final String PACKAGE_NAME = "packageName";
    protected static final String SEVERITY = "severity";
    protected static final String TYPE = "type";

    /**
     * Parses the specified file for issues.
     *
     * @param readerFactory
     *         provides a reader to the reports
     *
     * @return the issues
     * @throws ParsingException
     *         Signals that during parsing a non-recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the user has aborted the parsing
     */
    public abstract Report parse(ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException;

    /**
     * Parses the specified file for issues. Invokes the parser using {@link #parse(ReaderFactory)} and sets the file
     * name of the report.
     *
     * @param readerFactory
     *         provides a reader to the reports
     *
     * @return the issues
     * @throws ParsingException
     *         Signals that during parsing a non-recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the user has aborted the parsing
     */
    public Report parseFile(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        Report report = parse(readerFactory);
        report.setOriginReportFile(readerFactory.getFileName());
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
        catch (ParsingException | SecureXmlParserFactory.ParsingException ignored) {
            return false;
        }
    }

    /**
     * <p>Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters, ignoring case.</p>
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered equal. The comparison is <strong>case insensitive</strong>.</p>
     *
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, "abc")  = false
     * equalsIgnoreCase("abc", null)  = false
     * equalsIgnoreCase("abc", "abc") = true
     * equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     *
     * @param a
     *         the first CharSequence, may be {@code null}
     * @param b
     *         the second CharSequence, may be {@code null}
     *
     * @return {@code true} if the CharSequences are equal (case-insensitive), or both {@code null}
     */
    public static boolean equalsIgnoreCase(@CheckForNull final String a, @CheckForNull final String b) {
        return StringUtils.equals(normalize(a), normalize(b));
    }

    private static String normalize(@CheckForNull final String input) {
        return StringUtils.defaultString(input).toUpperCase(Locale.ENGLISH);
    }
}

