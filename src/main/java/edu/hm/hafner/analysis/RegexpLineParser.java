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

    private int currentLine = 0;

    /**
     * Creates a new instance of {@link RegexpLineParser}.
     *
     * @param id             ID of the parser
     * @param warningPattern pattern of compiler warnings.
     */
    protected RegexpLineParser(final String id, final String warningPattern) {
        super(id, warningPattern, false);
    }

    @Override
    public Issues parse(final Reader reader) throws ParsingCanceledException {
        Issues issues = new Issues();
        LineIterator iterator = IOUtils.lineIterator(reader);
        try {
            currentLine = 0;
            while (iterator.hasNext()) {
                findAnnotations(processLine(iterator.nextLine()), issues);
                currentLine++;
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
}
