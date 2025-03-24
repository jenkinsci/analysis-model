package edu.hm.hafner.analysis.parser;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser for the MSBuild/PcLint compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class MsBuildParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -2141974437420906595L;

    /** Pattern for command line warnings in MSBuild output. */
    public static final String COMMAND_LINE_WARNING_PATTERN =
            "(?:^(?:.*)Command line warning ([A-Za-z0-9]+):\\s*(.*)\\s*\\[(.*)\\])"; // Group 1 - 3

    /** Pattern for optional line number in MSBuild output. */
    public static final String OPTIONAL_LINE_NUMBER_PATTERN =
            "(?:\\s*(?:\\d+|\\d+:\\d+)>)?"; // Optional Group

    /** Pattern for different line column combinations in MSBuild output. */
    private static final String LINE_COLUMN_PATTERN =
            "\\((?:(\\d+),(\\d+),(\\d+),(\\d+)|(\\d+)(?:-(\\d+))?(?:,(\\d+)(?:-(\\d+))?)?)\\)"; // Group 5 - 12

    /** Pattern for file name in MSBuild output. */
    public static final String FILE_NAME_PATTERN =
            "(?:(?:(?:(.*?)" // Group 4
                    + LINE_COLUMN_PATTERN
                    + "|.*LINK)\\s*:|(.*):)"; // Group 13

    /** Pattern for severity type in MSBuild output. */
    public static final String SEVERITY_PATTERN =
            "\\s*([A-z-_]*\\s(?:[Nn]ote|[Ii]nfo|[Ww]arning|(?:fatal\\s*)?[Ee]rror))[^A-Za-z0-9]\\s*:?\\s*"; // Group 14

    /** Pattern for category of the issue in MSBuild output. */
    public static final String CATEGORY_PATTERN =
            "([A-Za-z0-9\\-_]+)?\\s*:\\s"; // Group 15

    /** Pattern for type of the issue in MSBuild output. */
    public static final String TYPE_PATTERN =
            "(?:\\s*([A-Za-z0-9.]+)\\s*:)?\\s*"; // Group 16

    /** Pattern for message in MSBuild output. */
    public static final String MESSAGE_PATTERN =
            "(.*?)"; // Group 17

    /** Pattern for project directory in MSBuild output. */
    public static final String PROJECT_DIR_PATTERN =
            "(?: \\[([^\\]]*)[/\\\\][^\\]\\\\]+\\])?"; // Group 18

    private static final String MS_BUILD_WARNING_PATTERN
            = COMMAND_LINE_WARNING_PATTERN
            + "|"
            + ANT_TASK + "(?:"
            + OPTIONAL_LINE_NUMBER_PATTERN
            + FILE_NAME_PATTERN
            + SEVERITY_PATTERN
            + CATEGORY_PATTERN
            + TYPE_PATTERN
            + MESSAGE_PATTERN
            + PROJECT_DIR_PATTERN
            + "))$";

    private static final Pattern IGNORED_TOOLS_PATTERN = Pattern.compile("(?!.exe)(\\.[^.]+)$");
    private static final Pattern LINKER_CAUSE = Pattern.compile(".*imported by '([A-Za-z0-9\\-_.]+)'.*");
    private static final String EXPECTED_CATEGORY = "Expected";
    private static final String MSBUILD = "MSBUILD";

    /**
     * Creates a new instance of {@link MsBuildParser}.
     */
    public MsBuildParser() {
        super(MS_BUILD_WARNING_PATTERN);
    }

    /**
     * Overrides the default implementation of 4000-character limit
     * Returns true regardless of the length.
     */
    @Override
    protected boolean isLineInteresting(final String line) {
        return true;
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
              final IssueBuilder builder) {
        var fileName = determineFileName(matcher);

        var fileExtensionMatcher = IGNORED_TOOLS_PATTERN.matcher(fileName);
        if (!fileExtensionMatcher.find()) {
            return Optional.empty();
        }

        builder.setFileName(fileName);

        if (StringUtils.isNotBlank(matcher.group(2))) {
            return builder.setLineStart(0)
                    .setCategory(matcher.group(1))
                    .setMessage(matcher.group(2))
                    .setSeverity(Severity.WARNING_NORMAL)
                    .buildOptional();
        }
        if (StringUtils.isNotEmpty(matcher.group(5))) {
            builder.setLineStart(matcher.group(5))
                    .setColumnStart(matcher.group(6))
                    .setLineEnd(matcher.group(7))
                    .setColumnEnd(matcher.group(8));
        }
        if (StringUtils.isNotEmpty(matcher.group(9))) {
            builder.setLineStart(matcher.group(9))
                    .setColumnStart(matcher.group(11))
                    .setLineEnd(matcher.group(10))
                    .setColumnEnd(matcher.group(12));
        }
        if (StringUtils.isNotEmpty(matcher.group(16))) {
            return builder.setCategory(matcher.group(15))
                    .setType(matcher.group(16))
                    .setMessage(matcher.group(17))
                    .setSeverity(Severity.guessFromString(matcher.group(14)))
                    .buildOptional();
        }

        var category = matcher.group(15);
        if (EXPECTED_CATEGORY.equals(category)) {
            return Optional.empty();
        }
        return builder.setCategory(category)
                .setMessage(matcher.group(17))
                .setSeverity(Severity.guessFromString(matcher.group(14)))
                .buildOptional();
    }

    /**
     * Determines the name of the file that is the cause of the warning.
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
        else if (StringUtils.isNotBlank(matcher.group(13))) {
            fileName = matcher.group(13);
        }
        else {
            fileName = matcher.group(4);
        }
        if (StringUtils.isBlank(fileName)) {
            var linker = LINKER_CAUSE.matcher(matcher.group(17));
            if (linker.matches()) {
                return linker.group(1);
            }
        }
        if (StringUtils.isBlank(fileName)) {
            fileName = StringUtils.substringBetween(matcher.group(17), "'");
        }
        if (StringUtils.isBlank(fileName)) {
            fileName = "unknown.file";
        }

        var projectDir = matcher.group(18);
        if (canResolveRelativeFileName(fileName, projectDir)) {
            fileName = FilenameUtils.concat(projectDir, fileName);
        }
        if (MSBUILD.equals(fileName.trim())) {
            fileName = "-";
        }
        return fileName;
    }

    private boolean canResolveRelativeFileName(final String fileName, final String projectDir) {
        return StringUtils.isNotBlank(projectDir) && FilenameUtils.getPrefixLength(fileName) == 0
                && !MSBUILD.equals(fileName.trim());
    }
}
