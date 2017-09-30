package edu.hm.hafner.analysis;

import javax.annotation.CheckReturnValue;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.Ensure;

/**
 * Parses an input stream for compiler warnings and returns the found issues. If your parser is based on a regular
 * expression you can extend from the existing base classes {@link RegexpLineParser} or {@link RegexpDocumentParser}.
 *
 * @author Ullrich Hafner
 *
 * @see RegexpLineParser
 * @see RegexpDocumentParser
 * @see edu.hm.hafner.analysis.parser.CppLintParser
 * @see edu.hm.hafner.analysis.parser.EclipseParser
 * @see edu.hm.hafner.analysis.parser.StyleCopParser
 */
public abstract class AbstractWarningsParser implements Serializable {
    private static final long serialVersionUID = 8466657735514387654L;

    public static final String DEPRECATION = "Deprecation";
    public static final String PROPRIETARY_API = "Proprietary API";

    private final String id;
    private PostProcessor postProcessor = new NullPostProcessor();

    /**
     * Creates a new instance of {@link AbstractWarningsParser}.
     *
     * @param id ID of the parser
     */
    protected AbstractWarningsParser(final String id) {
        this.id = id;
    }

    /**
     * Parses the specified input stream for issues. Note that the implementor of this method must not close the given
     * reader, this is done by the framework. FIXME: close is not done by framework?
     *
     * @param reader the reader to get the text from
     * @return the parsed issues
     * @throws ParsingCanceledException Signals that the parsing has been aborted by the user
     */
    public abstract Issues parse(final Reader reader) throws IOException;

    @Override
    public String toString() {
        return String.format("s (%s)", getId(), getClass().getSimpleName());
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
     * @param lineNumber the line number (as a string)
     * @return the line number
     */
    protected final int getLineNumber(final String lineNumber) {
        return convertLineNumber(lineNumber);
    }

    /**
     * Converts a string line number to an integer value. If the string is not a valid line number, then 0 is returned
     * which indicates a Issue at the top of the file.
     *
     * @param lineNumber the line number (as a string)
     * @return the line number
     */
    public static int convertLineNumber(final String lineNumber) {
        if (StringUtils.isNotBlank(lineNumber)) {
            try {
                return Integer.parseInt(lineNumber);
            }
            catch (NumberFormatException exception) {
                // ignore and return 0
            }
        }
        return 0;
    }

    /**
     * Classifies the warning message: tries to guess a category from the warning message.
     *
     * @param message the message to check
     * @return warning category, empty string if unknown
     */
    public static String classifyWarning(final String message) {
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
     * @param group   the warning category (might be empty)
     * @param message the warning message
     * @return the actual category
     */
    public static String classifyIfEmpty(final String group, final String message) {
        String category = StringUtils.capitalize(group);
        if (StringUtils.isEmpty(category)) {
            category = classifyWarning(message);
        }
        return category;
    }

    public void setPostProcessor(final PostProcessor postProcessor) {
        Ensure.that(postProcessor).isNotNull();

        this.postProcessor = postProcessor;
    }

    @CheckReturnValue
    protected String processLine(final String nextLine) {
        return postProcessor.processLine(nextLine);
    }

    @CheckReturnValue
    protected IssueBuilder issueBuilder() {
        return new IssueBuilder().setType(getId());
    }
}

