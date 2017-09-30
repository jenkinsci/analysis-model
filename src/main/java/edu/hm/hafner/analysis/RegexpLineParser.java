package edu.hm.hafner.analysis;

import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

/**
 * Parses an input stream line by line for compiler warnings using the provided regular expression. Multi-line regular
 * expressions are not supported, each warning has to be one a single line.
 *
 * @author Ullrich Hafner
 */
public abstract class RegexpLineParser extends RegexpParser {
    private static final long serialVersionUID = 5932670979793111138L;

    /**
     * Determines if a line is checked for a string existence before the regular expression is applied.
     *
     * @see #isLineInteresting(String)
     */
    private final boolean isStringMatchActivated;

    private int currentLine;

    /**
     * Creates a new instance of {@link RegexpDocumentParser}.
     *
     * @param id                     ID of the parser
     * @param warningPattern         pattern of compiler warnings.
     * @param isStringMatchActivated determines if a line is checked for a string existence before the regular
     *                               expression is applied
     */
    public RegexpLineParser(final String id, final String warningPattern, final boolean isStringMatchActivated) {
        super(id, warningPattern, false);

        this.isStringMatchActivated = isStringMatchActivated;
    }

    /**
     * Creates a new instance of {@link RegexpDocumentParser}.
     *
     * @param id             ID of the parser
     * @param warningPattern pattern of compiler warnings.
     */
    public RegexpLineParser(final String id, final String warningPattern) {
        this(id, warningPattern, false);
    }

    @Override
    public Issues parse(final Reader file) throws ParsingCanceledException {
        Issues issues = new Issues();
        LineIterator iterator = IOUtils.lineIterator(file);
        try {
            currentLine = 0;
            if (isStringMatchActivated) {
                while (iterator.hasNext()) {
                    String line = processLine(iterator.nextLine());
                    if (isLineInteresting(line)) {
                        findAnnotations(line, issues);
                    }
                    currentLine++;
                }
            }
            else {
                while (iterator.hasNext()) {
                    findAnnotations(processLine(iterator.nextLine()), issues);
                    currentLine++;
                }
            }
        }
        finally {
            iterator.close();
        }

        return postProcessWarnings(issues);
    }

    /**
     * Post processes the issues. This default implementation does nothing.
     *
     * @param issues the issues after the parsing process
     * @return the post processed issues
     */
    protected Issues postProcessWarnings(final Issues issues) {
        return issues;
    }

    /**
     * Returns the number of the current line in the parsed file.
     *
     * @return the current line
     */
    public int getCurrentLine() {
        return currentLine;
    }

    /**
     * Returns whether the specified line is interesting. Each interesting line will be handled by the defined regular
     * expression. Here a parser can implement some fast checks (i.e. string or character comparisons) in order to see
     * if a required condition is met.
     *
     * @param line the line to inspect
     * @return <code>true</code> if the line should be handed over to the regular expression scanner, <code>false</code>
     * if the line does not contain a warning.
     */
    protected boolean isLineInteresting(final String line) {
        return true;
    }

}
