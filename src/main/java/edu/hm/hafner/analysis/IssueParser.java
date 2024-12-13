package edu.hm.hafner.analysis;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.util.SecureXmlParserFactory;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Parses a file and returns the issues reported in this file.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("checkstyle:JavadocVariable")
public abstract class IssueParser implements Serializable {
    @Serial
    private static final long serialVersionUID  = 5L; // release 13.0.0

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

    private String defaultId = StringUtils.EMPTY;
    private String defaultName = StringUtils.EMPTY;

    /**
     * Parses a report (given by the reader factory) for issues.
     *
     * @param readerFactory
     *         factory to read input reports with a specific locale
     *
     * @return the report containing the found issues
     * @throws ParsingException
     *         signals that during parsing a non-recoverable error has been occurred
     * @throws ParsingCanceledException
     *         signals that the user has aborted the parsing
     */
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        return parse(readerFactory, defaultId, defaultName);
    }

    /**
     * Parses a report (given by the reader factory) for issues.
     *
     * @param readerFactory
     *         factory to read input reports with a specific locale
     * @param id
     *         the ID for the returned report
     * @param name
     *         a human-readable name for the returned report
     *
     * @return the report containing the found issues
     * @throws ParsingException
     *         signals that during parsing a non-recoverable error has been occurred
     * @throws ParsingCanceledException
     *         signals that the user has aborted the parsing
     */
    public Report parse(final ReaderFactory readerFactory, final String id, final String name) throws ParsingException, ParsingCanceledException {
        var report = parseReport(readerFactory);

        report.setOriginReportFile(readerFactory.getFileName());
        report.setElementType(getType());
        report.setOrigin(id, name);

        return report;
    }

    /**
     * Parses a report (given by the reader factory) for issues.
     *
     * @param readerFactory
     *         factory to read input reports with a specific locale
     *
     * @return the report containing the found issues
     * @throws ParsingException
     *         signals that during parsing a non-recoverable error has been occurred
     * @throws ParsingCanceledException
     *         signals that the user has aborted the parsing
     */
    protected abstract Report parseReport(ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException;

    public final void setDefaultId(final String defaultId) {
        this.defaultId = defaultId;
    }

    public final void setDefaultName(final String defaultName) {
        this.defaultName = defaultName;
    }

    // FIXME: back to descriptor?
    public IssueType getType() {
        return IssueType.WARNING;
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
     * Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters, ignoring case.
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered equal. The comparison is <strong>case-insensitive</strong>.</p>
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
        return StringUtils.equalsIgnoreCase(normalize(a), normalize(b));
    }

    private static String normalize(@CheckForNull final String input) {
        return StringUtils.defaultString(input).toUpperCase(Locale.ENGLISH);
    }
}
