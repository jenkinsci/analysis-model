package edu.hm.hafner.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.function.Function;

import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.parser.CppLintParser;
import edu.hm.hafner.analysis.parser.EclipseParser;
import edu.hm.hafner.analysis.parser.StyleCopParser;
import edu.hm.hafner.util.IntegerParser;
import edu.hm.hafner.util.VisibleForTesting;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Parses an input stream for issues of a specific static analysis tool and returns the found issues. If your parser is
 * based on a regular expression you can extend from the existing base classes {@link RegexpLineParser} or {@link
 * RegexpDocumentParser}. Each parser accepts a pre-preprocessor of the input lines. Here you can remove all debugging
 * lines that might be printed by the used build tool.
 *
 * @author Ullrich Hafner
 * @see RegexpLineParser
 * @see RegexpDocumentParser
 * @see CppLintParser
 * @see EclipseParser
 * @see StyleCopParser
 */
public abstract class AbstractParser extends IssueParser {
    private static final long serialVersionUID = 8466657735514387654L;

    /** Category for warnings due to usage of deprecate API. */
    public static final String DEPRECATION = "Deprecation";
    /** Category for warnings due to the usage of proprietary API. */
    public static final String PROPRIETARY_API = "Proprietary API";
    
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    @Override
    public Report parse(final File file, final Charset charset, final Function<String, String> preProcessor)
            throws ParsingException, ParsingCanceledException {
        fileName = file.getAbsolutePath();
        try (InputStream inputStream = new FileInputStream(file)) {
            return parse(inputStream, charset, preProcessor);
        }
        catch (FileNotFoundException exception) {
            throw new ParsingException(exception, "Can't find file: " + fileName);
        }
        catch (IOException exception) {
            throw new ParsingException(exception, "Can't scan file for issues: " + fileName);
        }
    }

    private Report parse(final InputStream inputStream, final Charset charset, final Function<String, String> preProcessor) {
        try (Reader input = createReader(inputStream, charset)) {
            return parse(input, preProcessor);
        }
        catch (IOException exception) {
            throw new ParsingException(exception, "Can't scan file for issues: " + fileName);
        }
    }

    private Reader createReader(final InputStream inputStream, final Charset charset) {
        return new InputStreamReader(new BOMInputStream(inputStream), charset);
    }

    /**
     * Parses the specified input stream for issues.
     *
     * @param reader
     *         the reader to get the text from
     * @param preProcessor
     *         pre processes each input line before handing it to the actual parser
     *
     * @return the issues
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the parsing has been aborted by the user
     */
    public abstract Report parse(Reader reader, Function<String, String> preProcessor)
            throws ParsingCanceledException, ParsingException;

    /**
     * Parses the specified input stream for issues. Input lines are not pre processed.
     *
     * @param reader
     *         the reader to get the text from
     *
     * @return the parsed issues
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the parsing has been aborted by the user
     */
    @VisibleForTesting
    public Report parse(final Reader reader) throws ParsingCanceledException, ParsingException {
        return parse(reader, Function.identity());
    }

    /**
     * Converts a string line number to an integer value. If the string is not a valid line number, then 0 is returned
     * which indicates an issue at the top of the file.
     *
     * @param lineNumber
     *         the line number (as a string)
     *
     * @return the line number
     */
    protected int parseInt(@CheckForNull final String lineNumber) {
        return new IntegerParser().parseInt(lineNumber);
    }

    /**
     * Classifies the warning message: tries to guess a category from the warning message.
     *
     * @param message
     *         the message to check
     *
     * @return warning category, empty string if unknown
     */
    protected String guessCategory(@CheckForNull final String message) {
        if (StringUtils.contains(message, "proprietary")) {
            return PROPRIETARY_API;
        }
        if (StringUtils.contains(message, "deprecated")) {
            return DEPRECATION;
        }
        return StringUtils.EMPTY;
    }

    /**
     * Returns a category for the current warning. If the provided category is not empty, then a capitalized string is
     * returned. Otherwise the category is obtained from the specified message text.
     *
     * @param category
     *         the warning category (might be empty)
     * @param message
     *         the warning message
     *
     * @return the actual category
     */
    protected String guessCategoryIfEmpty(@CheckForNull final String category, @CheckForNull final String message) {
        String capitalized = StringUtils.capitalize(category);
        if (StringUtils.isEmpty(capitalized)) {
            capitalized = guessCategory(message);
        }
        return capitalized;
    }
}

