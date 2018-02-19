package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;

/**
 * A parser for the ant javac compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class AntJavacParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 1737791073711198075L;

    private static final String ANT_JAVAC_WARNING_PATTERN = ANT_TASK + "\\s*(.*java):(\\d*):\\s*"
            + "(?:warning|\u8b66\u544a)\\s*:\\s*(?:\\[(\\w*)\\])?\\s*(.*)$"
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
        return line.contains("warning") || line.contains("\u8b66\u544a");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        if (StringUtils.isNotBlank(matcher.group(7))) {
            return builder.setFileName(matcher.group(7))
                    .setLineStart(0)
                    .setCategory(StringUtils.EMPTY)
                    .setMessage(matcher.group(8))
                    .build();
        }
        else if (StringUtils.isBlank(matcher.group(5))) {
            return builder.setFileName(matcher.group(1))
                    .setLineStart(parseInt(matcher.group(2)))
                    .setCategory(guessCategoryIfEmpty(matcher.group(3), matcher.group(4)))
                    .setMessage(matcher.group(4))
                    .build();
        }
        else {
            return builder.setFileName(matcher.group(6))
                    .setLineStart(0)
                    .setCategory("Path")
                    .setMessage(matcher.group(5))
                    .build();
        }
    }
}

