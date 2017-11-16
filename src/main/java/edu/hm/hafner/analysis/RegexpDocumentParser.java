package edu.hm.hafner.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Collectors;

/**
 * Parses an input stream as a whole document for compiler warnings using the provided regular expression.
 *
 * @author Ullrich Hafner
 */
public abstract class RegexpDocumentParser extends RegexpParser {
    private static final long serialVersionUID = -4985090860783261124L;

    /**
     * Creates a new instance of {@link RegexpDocumentParser}.
     *
     * @param warningPattern pattern of compiler warnings.
     * @param useMultiLine   Enables multi line mode. In multi line mode the expressions <tt>^</tt> and <tt>$</tt> match
     *                       just after or just before, respectively, a line terminator or the end of the input
     *                       sequence. By default these expressions only match at the beginning and the end of the
     */
    protected RegexpDocumentParser(final String warningPattern, final boolean useMultiLine) {
        super(warningPattern, useMultiLine);
    }

    @Override
    public Issues<Issue> parse(final Reader reader, final IssueBuilder builder) throws ParsingCanceledException {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String text = bufferedReader.lines().map(getTransformer()).collect(Collectors.joining("\n"));

            Issues<Issue> warnings = new Issues<>();
            findAnnotations(text + "\n", warnings, builder);
            return warnings;

        }
        catch (IOException e) {
            throw new ParsingException(e);
        }
    }
}
