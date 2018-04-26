package edu.hm.hafner.analysis;

import java.io.Reader;
import java.util.function.Function;

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
     * @param warningPattern pattern of compiler warnings.
     */
    protected RegexpLineParser(final String warningPattern) {
        super(warningPattern, false);
    }

    @Override
    public Issues parse(final Reader reader, final Function<String, String> preProcessor)
            throws ParsingCanceledException, ParsingException {
        Issues issues = new Issues();
        LineIterator iterator = IOUtils.lineIterator(reader);
        try {
            currentLine = 0;
            while (iterator.hasNext()) {
                String line = preProcessor.apply(iterator.nextLine());
                findIssues(line, issues);
                currentLine++;
            }
        }
        finally {
            iterator.close();
        }

        return postProcess(issues);
    }

    /**
     * Post processes the issues. This default implementation does nothing.
     *
     * @param issues  the issues after the parsing process
     * @return the post processed issues
     */
    protected Issues postProcess(final Issues issues) {
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
