package edu.hm.hafner.analysis;

import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

/**
 * Parses an input stream line by line for compiler warnings using the provided regular expression. Multi-line regular
 * expressions are not supported, each warning has to be one a single line. A parser must implement some fast checks
 * (i.e. string or character comparisons) before the line parsing is started, see {@link #isLineInteresting(String)}.
 *
 * @author Ullrich Hafner
 */
public abstract class FastRegexpLineParser extends RegexpLineParser {
    private int currentLine = 0;

    /**
     * Creates a new instance of {@link FastRegexpLineParser}.
     *
     * @param id             ID of the parser
     * @param warningPattern pattern of compiler warnings.
     */
    protected FastRegexpLineParser(final String id, final String warningPattern) {
        super(id, warningPattern);
    }

    @Override
    public Issues parse(final Reader reader) throws ParsingCanceledException {
        Issues issues = new Issues();
        LineIterator iterator = IOUtils.lineIterator(reader);
        try {
            currentLine = 0;
            while (iterator.hasNext()) {
                String line = processLine(iterator.nextLine());
                if (isLineInteresting(line)) {
                    findAnnotations(line, issues);
                }
                currentLine++;
            }
        }
        finally {
            iterator.close();
        }

        return postProcessWarnings(issues);
    }

    /**
     * Returns the number of the current line in the parsed file.
     *
     * @return the current line
     */
    @Override
    public int getCurrentLine() {
        return currentLine;
    }

    /**
     * Returns whether the specified line is interesting. Each interesting line will be handled by the defined regular
     * expression. Here a parser must implement some fast checks (i.e. string or character comparisons) in order to see
     * if a required condition is met.
     *
     * @param line the line to inspect
     * @return {@code true} if the line should be handed over to the regular expression scanner, {@code false} if the
     * line does not contain a warning.
     */
    protected abstract boolean isLineInteresting(String line);

}
