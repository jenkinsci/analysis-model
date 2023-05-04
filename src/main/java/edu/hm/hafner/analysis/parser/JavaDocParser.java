package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the ant JavaDoc compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class JavaDocParser extends AbstractMavenLogParser {
    private static final long serialVersionUID = 7127568148333474921L;
    private static final String JAVA_DOC_WARNING_PATTERN = "(?:\\s*\\[(?:javadoc|WARNING|ERROR)\\]\\s*)?(?:(?:(?:Exit"
            + " code: \\d* - )?(.*):(\\d+))|(?:\\s*javadoc\\s*)):\\s*(warning|error)\\s*[-:]\\s*(.*)";

    private static final Pattern TAG_PATTERN = Pattern.compile(".*(@\\w+).*");

    /**
     * Creates a new instance of {@link JavaDocParser}.
     */
    public JavaDocParser() {
        super(JAVA_DOC_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return super.isLineInteresting(line)
                && !hasGoals(MAVEN_COMPILER_PLUGIN)
                && lineContainsKeywords(line);
    }

    private boolean lineContainsKeywords(final String line) {
        return line.contains("javadoc")
                || line.contains(" @")
                || hasErrorPrefixAndErrorInMessage(line)
                || hasWarningsPrefixAndWarningInMessage(line);
    }

    private boolean hasErrorPrefixAndErrorInMessage(final String line) {
        return line.contains("error") && line.contains("ERROR");
    }

    private boolean hasWarningsPrefixAndWarningInMessage(final String line) {
        return line.contains("warning") && line.contains("WARNING");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String type = matcher.group(3);

        String message = matcher.group(4);
        Matcher tagMatcher = TAG_PATTERN.matcher(message);
        if (tagMatcher.matches()) {
            builder.setCategory(String.format("JavaDoc %s", tagMatcher.group(1)));
        }
        else {
            builder.setCategory("-");
        }
        return builder.setFileName(StringUtils.defaultIfEmpty(matcher.group(1), " - "))
                .setLineStart(matcher.group(2))
                .setMessage(message)
                .setSeverity(Severity.guessFromString(type))
                .buildOptional();
    }
}

