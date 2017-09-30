package edu.hm.hafner.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

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
     * @param id             ID of the parser
     * @param warningPattern pattern of compiler warnings.
     * @param useMultiLine   Enables multi line mode. In multi line mode the expressions <tt>^</tt> and <tt>$</tt> match
     *                       just after or just before, respectively, a line terminator or the end of the input
     *                       sequence. By default these expressions only match at the beginning and the end of the
     *                       entire input sequence.
     */
    public RegexpDocumentParser(final String id,
                                final String warningPattern, final boolean useMultiLine) {
        super(id, warningPattern, useMultiLine);
    }

    @Override
    public Issues parse(final Reader file) throws IOException, ParsingCanceledException {
        BufferedReader reader = new BufferedReader(file);
        StringBuilder buf = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            buf.append(processLine(line)).append('\n');
            line = reader.readLine();
        }

        String content = buf.toString();

        file.close();

        Issues warnings = new Issues();
        findAnnotations(content, warnings);
        return warnings;
    }
}
