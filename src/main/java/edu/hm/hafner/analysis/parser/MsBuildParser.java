package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the MSBuild/PcLint compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class MsBuildParser extends LookaheadParser {
    private static final long serialVersionUID = -2141974437420906595L;

    private static final String MS_BUILD_WARNING_PATTERN
            = "(?:^(?:.*)Command line warning ([A-Za-z0-9]+):\\s*(.*)\\s*\\[(.*)\\])|"
            + ANT_TASK + "(?:(?:\\s*(?:\\d+|\\d+:\\d+)>)?(?:(?:(?:(.*?)\\((\\d*)(?:,(\\d+))?[a-zA-Z]*?\\)|.*LINK)\\s*:|"
            + "(.*):)\\s*([A-z-_]*\\s(?:[Nn]ote|[Ii]nfo|[Ww]arning|(?:fatal\\s*)?[Ee]rror))[^A-Za-z0-9]\\s*:?\\s*([A-Za-z0-9\\-_]+)?"
            + "\\s*:\\s(?:\\s*([A-Za-z0-9.]+)\\s*:)?\\s*(.*?)(?: \\[([^\\]]*)[/\\\\][^\\]\\\\]+\\])?"
            + "|(.*)\\s*:.*error\\s*(LNK[0-9]+):\\s*(.*)))$";

    /**
     * Creates a new instance of {@link MsBuildParser}.
     */
    public MsBuildParser() {
        super(MS_BUILD_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        //builder.setFileName(determineFileName(matcher));

        //-------
        String fileName = determineFileName(matcher);

        Pattern fileExtensionPattern = Pattern.compile("(?!.exe)(\\.[^.]+)$");
        Matcher fileExtensionMatcher = fileExtensionPattern.matcher(fileName);
        if (!fileExtensionMatcher.find()) {
            return Optional.empty();
        }

        builder.setFileName(fileName);
        //-------

        if (StringUtils.isNotBlank(matcher.group(2))) {
            return builder.setLineStart(0)
                    .setCategory(matcher.group(1))
                    .setMessage(matcher.group(2))
                    .setSeverity(Severity.WARNING_NORMAL)
                    .buildOptional();
        }
        if (StringUtils.isNotBlank(matcher.group(13))) {
            return builder.setLineStart(0)
                    .setCategory(matcher.group(14))
                    .setType(matcher.group(13))
                    .setMessage(matcher.group(15))
                    .setSeverity(Severity.guessFromString(matcher.group(8)))
                    .buildOptional();
        }
        if (StringUtils.isNotEmpty(matcher.group(10))) {
            return builder.setLineStart(matcher.group(5))
                    .setColumnStart(matcher.group(6))
                    .setCategory(matcher.group(9))
                    .setType(matcher.group(10))
                    .setMessage(matcher.group(11))
                    .setSeverity(Severity.guessFromString(matcher.group(8)))
                    .buildOptional();
        }

        String category = matcher.group(9);
        if ("Expected".equals(category)) {
            return Optional.empty();
        }
        return builder.setLineStart(matcher.group(5))
                .setColumnStart(matcher.group(6))
                .setCategory(category)
                .setMessage(matcher.group(11))
                .setSeverity(Severity.guessFromString(matcher.group(8)))
                .buildOptional();
    }

    /**
     * Determines the name of the file that is cause of the warning.
     *
     * @param matcher
     *         the matcher to get the matches from
     *
     * @return the name of the file with a warning
     */
    private String determineFileName(final Matcher matcher) {
        String fileName;

        if (StringUtils.isNotBlank(matcher.group(3))) {
            fileName = matcher.group(3);
        }
        else if (StringUtils.isNotBlank(matcher.group(7))) {
            fileName = matcher.group(7);
        }
        else if (StringUtils.isNotBlank(matcher.group(13))) {
            fileName = matcher.group(13);
        }
        else {
            fileName = matcher.group(4);
        }
        if (StringUtils.isBlank(fileName)) {
            fileName = StringUtils.substringBetween(matcher.group(11), "'");
        }
        if (StringUtils.isBlank(fileName)) {
            fileName = "unknown.file";
        }

        String projectDir = matcher.group(12);
        if (canResolveRelativeFileName(fileName, projectDir)) {
            fileName = FilenameUtils.concat(projectDir, fileName);
        }
        if ("MSBUILD".equals(fileName.trim())) {
            fileName = "-";
        }
        return fileName;
    }

    private boolean canResolveRelativeFileName(final String fileName, final String projectDir) {
        return StringUtils.isNotBlank(projectDir) && FilenameUtils.getPrefixLength(fileName) == 0
                && !"MSBUILD".equals(fileName.trim());
    }
}

