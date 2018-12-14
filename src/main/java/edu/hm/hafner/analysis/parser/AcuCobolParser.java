package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import static edu.hm.hafner.analysis.Categories.guessCategory;
import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;

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
        super(ACU_COBOL_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(2))
                .setLineStart(matcher.group(3))
                .setCategory(guessCategory(matcher.group(4)))
                .setMessage(matcher.group(4))
                .buildOptional();
    }
}

