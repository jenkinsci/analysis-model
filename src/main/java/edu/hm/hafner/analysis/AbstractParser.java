package edu.hm.hafner.analysis;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.function.Function;

import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Functions;

import edu.hm.hafner.util.Ensure;

/**
 * Parses an input stream for compiler warnings and returns the found issues. If your parser is based on a regular
 * expression you can extend from the existing base classes {@link RegexpLineParser} or {@link RegexpDocumentParser}.
 *
 * @author Ullrich Hafner
 * @see RegexpLineParser
 * @see RegexpDocumentParser
 * @see edu.hm.hafner.analysis.parser.CppLintParser
 * @see edu.hm.hafner.analysis.parser.EclipseParser
 * @see edu.hm.hafner.analysis.parser.StyleCopParser
 */
// FIXME: type is not ID, module could be automatically set in Builder
public abstract class AbstractParser implements Serializable {
    private static final long serialVersionUID = 8466657735514387654L;

    /** Category for warnings due to usage of deprecate API. */
    public static final String DEPRECATION = "Deprecation";
    /** Category for warnings due to the usage of proprietary API. */
    public static final String PROPRIETARY_API = "Proprietary API";

    private final String id;

    private transient Function<String, String> transformer = Functions.identity();
    private transient Charset charset;
    private transient String moduleName = StringUtils.EMPTY;

    /**
     * Creates a new instance of {@link AbstractParser}.
     *
     * @param id
     *         ID of the parser
     */
    protected AbstractParser(final String id) {
        this.id = id;
    }

    public Issues parse(final File file, final Charset charset, final String moduleName) throws ParsingException {
        this.charset = charset;
        this.moduleName = moduleName;
        try (Reader input = createReader(new FileInputStream(file))) {
            return parse(input);
        }
        catch (IOException exception) {
            throw new ParsingException(exception, "Can't scan file for warnings: " + file.getAbsolutePath());
        }
    }

    private Reader createReader(final InputStream inputStream) {
        return new InputStreamReader(new BOMInputStream(inputStream), charset);
    }

    /**
     * Parses the specified input stream for issues.
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
    public abstract Issues parse(Reader reader) throws ParsingCanceledException, ParsingException;

    @Override
    public String toString() {
        return String.format("%s (%s)", getId(), getClass().getSimpleName());
    }

    /**
     * Returns the ID of this parser.
     *
     * @return the ID of this parser
     */
    public String getId() {
        return id;
    }

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
        return ObjectUtils.defaultIfNull(transformer, Functions.identity());
    }

    /**
     * Returns a new issue builder that has the ID of this warning set as type property.
     *
     * @return a new issue builder
     */
    @CheckReturnValue
    // TODO: issue builder should be part of parse method API then no field is required
    protected IssueBuilder issueBuilder() {
        return new IssueBuilder().setType(getId()).setModuleName(moduleName);
    }
}

