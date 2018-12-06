package edu.hm.hafner.analysis;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

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
     * @param warningPattern
     *         pattern of compiler warnings.
     */
    protected RegexpLineParser(final String warningPattern) {
        super(warningPattern, false);
    }

    @Override
    public Report parse(final ReaderFactory reader) throws ParsingException {
        Report report = new Report();
        try (Stream<String> lines = reader.readStream()) {
            currentLine = 1;
            Iterator<String> iterator = lines.iterator();
            while (iterator.hasNext()) {
                findIssues(iterator.next(), report);
                
                currentLine++;
            }
        }

        return postProcess(report);
    }

    /**
     * Post processes the issues. This default implementation does nothing.
     *
     * @param report
     *         the issues after the parsing process
     *
     * @return the post processed issues
     */
    protected Report postProcess(final Report report) {
        return report;
    }

    /**
     * Returns the number of the current line in the parsed file.
     *
     * @return the current line
     */
    protected int getCurrentLine() {
        return currentLine;
    }
}
