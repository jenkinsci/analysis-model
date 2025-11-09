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
            "(?:(?:(?:(.*?)" // Group 9 - filename with line/column
                    + LINE_COLUMN_PATTERN // Groups 10-17 - line/column combinations
                    + "|.*LINK)\\s*:|(.*):)"; // Group 18 - simple filename

    /** Pattern for severity type in MSBuild output. */
    public static final String SEVERITY_PATTERN =
            "\\s*((?:[A-z-_]+\\s)?(?:[Nn]ote|[Ii]nfo|[Ww]arning|[Hh]int|(?:fatal\\s*)?[Ee]rror))[^A-Za-z0-9]\\s*:?\\s*"; // Group 19

    /** Pattern for category of the issue in MSBuild output. */
    public static final String CATEGORY_PATTERN =
            "([A-Za-z0-9\\-_]+)?\\s*:\\s"; // Group 20

    /** Pattern for type of the issue in MSBuild output. */
    public static final String TYPE_PATTERN =
            "(?:\\s*([A-Za-z0-9.]+)\\s*:)?\\s*"; // Group 21

    /** Pattern for message in MSBuild output. */
    public static final String MESSAGE_PATTERN =
            "(.*?)"; // Group 22

    /** Pattern for project directory in MSBuild output. */
    public static final String PROJECT_DIR_PATTERN =
            "(?: \\[([^\\]]*)[/\\\\][^\\]\\\\]+\\])?"; // Group 23

    /**
     * Pattern for Delphi compiler hints/warnings (simplified format without standard MSBuild prefix).
     */
    // Groups 4-8: 4=filename, 5=line, 6=severity, 7=category, 8=message
    private static final String DELPHI_SIMPLE_PATTERN = "^\\s*([A-Za-z]:[^\\(]+\\.(?:pas|dpr|dpk|dproj))\\((\\d+)\\)\\s+([Ww]arning|[Hh]int)\\s*:\\s*([A-Za-z0-9]+)\\s+(.*)$";

    private static final String MS_BUILD_WARNING_PATTERN = COMMAND_LINE_WARNING_PATTERN
            + "|"
            + DELPHI_SIMPLE_PATTERN
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

    /**
     * Pattern to match valid source code file extensions. This pattern excludes:
     * - Files without extensions (like tool names: NMAKE, rs, etc.)
     * - Binary/executable files (exe, dll, so, etc.)
     * - Archive files (jar, war, zip, gz, bz2, 7z)
     * - Object files and libraries (o, a, lib)
     * <p>
     * The pattern requires a file extension that is NOT one of the excluded types.
     * This aligns with the NON_SOURCE_CODE_EXTENSIONS set in FingerprintGenerator.
     */
    private static final Pattern VALID_SOURCE_FILE_PATTERN = Pattern.compile(
            ".*\\.(?!(?i)(?:o|exe|dll|so|a|lib|jar|war|zip|7z|gz|bz2)$)[^.]+$");
    
    private static final Pattern LINKER_CAUSE = Pattern.compile(".*imported by '([A-Za-z0-9\\-_.]+)'.*");
    private static final String EXPECTED_CATEGORY = "Expected";
    private static final String MSBUILD = "MSBUILD";
    private static final Pattern HEADER_COMPILE_MESSAGE = Pattern.compile("\\(compiling source file .*\\)");

    // Pattern to extract Delphi file path from message (e.g., "C:\Path\File.pas(123) Warning: ...")
    private static final Pattern DELPHI_FILE_PATTERN = Pattern.compile(
            "^\\s*([^:]+\\.(?:pas|dpr|dpk|dproj))\\((\\d+)\\)\\s+(?:[Ww]arning|[Hh]int)\\s*:\\s*([A-Za-z0-9]+)\\s+(.*)$");

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
        // Check if this matches the Delphi simple pattern (groups 4-8)
        if (StringUtils.isNotBlank(matcher.group(4))) {
            return createDelphiSimpleIssue(matcher, builder);
        }

        var fileName = determineFileName(matcher);

        // Skip files that are not valid source code files (tools, executables, archives, etc.)
        if (!VALID_SOURCE_FILE_PATTERN.matcher(fileName).matches()) {
            return Optional.empty();
        }

        // Check if this is a Delphi warning/hint where the actual file is in the message
        var message = matcher.group(22);
        var delphiIssue = createDelphiEmbeddedIssue(matcher, message, builder);
        if (delphiIssue.isPresent()) {
            return delphiIssue;
        }

        builder.setFileName(fileName);

        // Command line warning pattern (groups 1-3)
        if (StringUtils.isNotBlank(matcher.group(2))) {
            return builder.setLineStart(0)
                    .setCategory(matcher.group(1))
                    .setMessage(matcher.group(2))
                    .setSeverity(Severity.WARNING_NORMAL)
                    .buildOptional();
        }

        setLineAndColumnRanges(matcher, builder);

        // Type pattern (group 21)
        if (StringUtils.isNotEmpty(matcher.group(21))) {
            return builder.setCategory(matcher.group(20))
                    .setType(matcher.group(21))
                    .setMessage(matcher.group(22))
                    .setSeverity(Severity.guessFromString(matcher.group(19)))
                    .buildOptional();
        }

        return createStandardIssue(matcher, message, builder);
    }

    private Optional<Issue> createDelphiSimpleIssue(final Matcher matcher, final IssueBuilder builder) {
        var delphiMessage = matcher.group(8);
        if (delphiMessage == null) {
            delphiMessage = "";
        }
        return builder.setFileName(matcher.group(4))
                .setLineStart(matcher.group(5))
                .setCategory(matcher.group(7))
                .setMessage(delphiMessage)
                .setSeverity(Severity.guessFromString(matcher.group(6)))
                .buildOptional();
    }

    private Optional<Issue> createDelphiEmbeddedIssue(final Matcher matcher, final String message,
            final IssueBuilder builder) {
        if (message != null) {
            var delphiMatcher = DELPHI_FILE_PATTERN.matcher(message);
            if (delphiMatcher.matches()) {
                var fileName = delphiMatcher.group(1);
                var lineNumber = delphiMatcher.group(2);
                var delphiCategory = delphiMatcher.group(3);
                var delphiMessage = delphiMatcher.group(4);

                return builder.setFileName(fileName)
                        .setLineStart(lineNumber)
                        .setCategory(delphiCategory)
                        .setMessage(delphiMessage)
                        .setSeverity(Severity.guessFromString(matcher.group(19)))
                        .buildOptional();
            }
        }
        return Optional.empty();
    }

    private void setLineAndColumnRanges(final Matcher matcher, final IssueBuilder builder) {
        // Line/column groups (groups 10-13 and 14-17)
        if (StringUtils.isNotEmpty(matcher.group(10))) {
            builder.setLineStart(matcher.group(10))
                    .setColumnStart(matcher.group(11))
                    .setLineEnd(matcher.group(12))
                    .setColumnEnd(matcher.group(13));
        }
        if (StringUtils.isNotEmpty(matcher.group(14))) {
            builder.setLineStart(matcher.group(14))
                    .setColumnStart(matcher.group(16))
                    .setLineEnd(matcher.group(15))
                    .setColumnEnd(matcher.group(17));
        }
    }

    private Optional<Issue> createStandardIssue(final Matcher matcher, final String message,
            final IssueBuilder builder) {
        var category = matcher.group(20);
        if (EXPECTED_CATEGORY.equals(category)) {
            return Optional.empty();
        }
        var messageText = message == null ? "" : message;
        var cleanedMessage = HEADER_COMPILE_MESSAGE.matcher(messageText).replaceAll(StringUtils.EMPTY);
        return builder.setCategory(category)
                .setMessage(cleanedMessage)
                .setSeverity(Severity.guessFromString(matcher.group(19)))
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
        String fileName = extractFileNameFromGroups(matcher);
        fileName = resolveFileName(fileName, matcher);
        return normalizeFileName(fileName, matcher.group(23));
    }

    private String extractFileNameFromGroups(final Matcher matcher) {
        // Group 3 is from COMMAND_LINE_WARNING_PATTERN
        if (StringUtils.isNotBlank(matcher.group(3))) {
            return matcher.group(3);
        }
        // Group 18 is simple filename from FILE_NAME_PATTERN
        if (StringUtils.isNotBlank(matcher.group(18))) {
            return matcher.group(18);
        }
        // Group 9 is filename with line/column from FILE_NAME_PATTERN
        return matcher.group(9);
    }

    private String resolveFileName(final String fileName, final Matcher matcher) {
        if (StringUtils.isNotBlank(fileName)) {
            return fileName;
        }

        // Group 22 is message
        var linker = LINKER_CAUSE.matcher(matcher.group(22));
        if (linker.matches()) {
            return linker.group(1);
        }

        String extractedFileName = StringUtils.substringBetween(matcher.group(22), "'");
        return StringUtils.isNotBlank(extractedFileName) ? extractedFileName : "unknown.file";
    }

    private String normalizeFileName(final String fileName, final String projectDir) {
        String normalizedFileName = fileName;

        if (canResolveRelativeFileName(normalizedFileName, projectDir)) {
            normalizedFileName = FilenameUtils.concat(projectDir, normalizedFileName);
        }
        if (MSBUILD.equals(normalizedFileName.trim())) {
            return "-";
        }
        if (containsInvalidPathCharacters(normalizedFileName)) {
            return "-";
        }
        return normalizedFileName;
    }

    private boolean containsInvalidPathCharacters(final String fileName) {
        if (fileName.contains("<") || fileName.contains(">") || fileName.contains("|")) {
            return true;
        }
        return fileName.contains("\"") || fileName.contains("?") || fileName.contains("*");
    }

    private boolean canResolveRelativeFileName(final String fileName, final String projectDir) {
        return StringUtils.isNotBlank(projectDir) && FilenameUtils.getPrefixLength(fileName) == 0
                && !MSBUILD.equals(fileName.trim());
    }
}