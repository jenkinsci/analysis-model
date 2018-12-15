package edu.hm.hafner.analysis;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Parses an input stream line by line for compiler warnings using the provided regular expression. Multi-line regular
 * expressions are not supported, each warning has to be one a single line.
 *
 * @author Ullrich Hafner
 */
public abstract class RegexpLineParser extends RegexpParser {
    private static final long serialVersionUID = 5932670979793111138L;

    private static final Pattern MAKE_PATH = Pattern.compile(".*make(?:\\[\\d+])?: Entering directory `(.*)'");

    private int currentLine = 0;
    private String currentDirectory = StringUtils.EMPTY;

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
            Iterator<String> iterator = lines.iterator();
            for (currentLine = 1; iterator.hasNext(); currentLine++) {
                String line = iterator.next();
                Matcher makeLineMatcher = MAKE_PATH.matcher(line);
                if (makeLineMatcher.matches()) {
                    currentDirectory = makeLineMatcher.group(1);
                }
                findIssues(line, report);
            }
        }

        return postProcess(report);
    }

    @Override
    protected IssueBuilder configureIssueBuilder(final IssueBuilder builder) {
        return builder.setDirectory(currentDirectory);
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
