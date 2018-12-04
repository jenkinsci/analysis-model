package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import static edu.hm.hafner.analysis.Categories.guessCategoryIfEmpty;
import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the ant javac compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class AntJavacParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 1737791073711198075L;

    private static final String ANT_JAVAC_WARNING_PATTERN = ANT_TASK + "\\s*(.*java):(\\d*):\\s*"
            + "(warning|error|\u8b66\u544a)\\s*:\\s*(?:\\[(\\w*)\\])?\\s*(.*)$"
            + "|^\\s*\\[.*\\]\\s*warning.*\\]\\s*(.*\"("
            + ".*)\".*)$" + "|^(.*class)\\s*:\\s*warning\\s*:\\s*(.*)$";
    // \u8b66\u544a is Japanese l10n

    /**
     * Creates a new instance of {@link AntJavacParser}.
     */
    public AntJavacParser() {
        super(ANT_JAVAC_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("warning") || line.contains("error") || line.contains("\u8b66\u544a");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        builder.setSeverity(mapSeverity(matcher.group(3)));
        if (StringUtils.isNotBlank(matcher.group(8))) {
            return builder.setFileName(matcher.group(8))
                    .setLineStart(0)
                    .setCategory(StringUtils.EMPTY)
                    .setMessage(matcher.group(9))
                    .build();
        }
        else if (StringUtils.isBlank(matcher.group(6))) {
            return builder.setFileName(matcher.group(1))
                    .setLineStart(matcher.group(2))
                    .setCategory(guessCategoryIfEmpty(matcher.group(4), matcher.group(5)))
                    .setMessage(matcher.group(5))
                    .build();
        }
        else {
            return builder.setFileName(matcher.group(7))
                    .setLineStart(0)
                    .setCategory("Path")
                    .setMessage(matcher.group(6))
                    .build();
        }
    }

    private Severity mapSeverity(final String type) {
        return "error".equalsIgnoreCase(type) ? Severity.ERROR : Severity.WARNING_NORMAL;
    }
}

