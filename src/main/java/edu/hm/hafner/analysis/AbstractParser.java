package edu.hm.hafner.analysis;

import javax.annotation.CheckForNull;
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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.parser.CppLintParser;
import edu.hm.hafner.analysis.parser.EclipseParser;
import edu.hm.hafner.analysis.parser.StyleCopParser;
import edu.hm.hafner.util.Ensure;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import static java.util.function.Function.*;

/**
 * Parses an input stream for issues of a specific static analysis tool and returns the found issues. If your parser is
 * based on a regular expression you can extend from the existing base classes {@link RegexpLineParser} or {@link
 * RegexpDocumentParser}.
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

    @SuppressFBWarnings(value = "SE", justification = "Getter handles null")
    private transient Function<String, String> transformer = identity();

    @Override
    public Issues<Issue> parse(final File file, final Charset charset, final IssueBuilder builder)
            throws ParsingException, ParsingCanceledException {
        try (Reader input = createReader(new FileInputStream(file), charset)) {
            Issues<Issue> issues = parse(input, builder);
            issues.logInfo("Successfully parsed '%s': found %d issues",
                    file.getAbsolutePath(), issues.getSize());
            if (issues.getDuplicatesSize() == 1) {
                issues.logInfo("Note: one issue has been dropped since it is a duplicate");
            }
            else if (issues.getDuplicatesSize() > 1) {
                issues.logInfo("Note: %d issues have been dropped since they are duplicates",
                        issues.getDuplicatesSize());
            }
            return issues;
        }
        catch (FileNotFoundException exception) {
            throw new ParsingException(exception, "Can't find file: " + file.getAbsolutePath());
        }
        catch (IOException exception) {
            throw new ParsingException(exception, "Can't scan file for issues: " + file.getAbsolutePath());
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
     * @param builder
     *         the issue builder to use
     *
     * @return the parsed issues
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     * @throws ParsingCanceledException
     *         Signals that the parsing has been aborted by the user
     */
    public abstract Issues<Issue> parse(Reader reader, IssueBuilder builder)
            throws ParsingCanceledException, ParsingException;

    /**
     * Converts a string line number to an integer value. If the string is not a valid line number, then 0 is returned
     * which indicates a Issue at the top of the file.
     *
     * @param lineNumber
     *         the line number (as a string)
     *
     * @return the line number
     */
    public int parseInt(@CheckForNull final String lineNumber) {
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
    public String guessCategory(@CheckForNull final String message) {
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
    public String guessCategoryIfEmpty(@CheckForNull final String category, @CheckForNull final String message) {
        String capitalized = StringUtils.capitalize(category);
        if (StringUtils.isEmpty(capitalized)) {
            capitalized = guessCategory(message);
        }
        return capitalized;
    }

    /**
     * Sets an optional line transformer. This transformer will be called during parsing: each line of the input file
     * will be transformed using the provided transformer before it is handed over to the actual parser.
     *
     * @param transformer
     *         the transformer
     */
    public void setTransformer(final Function<String, String> transformer) {
        Ensure.that(transformer).isNotNull();

        this.transformer = transformer;
    }

    /**
     * Returns the specified line transformer. If no such transformer has been set then a transformer is returned that
     * does not modify the input lines.
     *
     * @return the transformer to use
     */
    public Function<String, String> getTransformer() {
        return ObjectUtils.defaultIfNull(transformer, identity());
    }
}

