package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;

/**
 * A parser for the Acu Cobol compile.
 *
 * @author jerryshea
 */
public class AcuCobolParser extends FastRegexpLineParser {
    private static final long serialVersionUID = -894639209290549425L;
    private static final String ACU_COBOL_WARNING_PATTERN = "^\\s*(\\[.*\\])?\\s*?(.*), line ([0-9]*): Warning: (.*)$";

    /**
     * Creates a new instance of {@link AcuCobolParser}.
     */
    public AcuCobolParser() {
        super("acu-cobol", ACU_COBOL_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning");
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        String message = matcher.group(4);
        String category = guessCategory(message);
        return issueBuilder().setFileName(matcher.group(2)).setLineStart(parseInt(matcher.group(3)))
                             .setCategory(category).setMessage(message).build();
    }
}

