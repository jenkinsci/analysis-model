package edu.hm.hafner.analysis;

import javax.annotation.Nonnull;
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
    public Issues<Issue> parse(@Nonnull final Reader reader, @Nonnull final IssueBuilder builder,
            final Function<String, String> preProcessor) throws ParsingCanceledException {
        Issues<Issue> issues = new Issues<>();
        LineIterator iterator = IOUtils.lineIterator(reader);
        try {
            currentLine = 0;
            while (iterator.hasNext()) {
                String line = preProcessor.apply(iterator.nextLine());
                findAnnotations(line, issues, builder);
                currentLine++;
            }
        }
        finally {
            iterator.close();
        }

        return postProcessWarnings(issues, builder);
    }

    /**
     * Post processes the issues. This default implementation does nothing.
     *
     * @param issues  the issues after the parsing process
     * @param builder the builder to create the issues with
     * @return the post processed issues
     */
    protected Issues<Issue> postProcessWarnings(final Issues<Issue> issues, final IssueBuilder builder) {
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
