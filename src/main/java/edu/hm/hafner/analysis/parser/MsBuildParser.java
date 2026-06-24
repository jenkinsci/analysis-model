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
     * Pattern to identify bare tool names that should be ignored (without path separators).
     * Only matches tool names when they appear alone, not as part of a path.
     */
    private static final Pattern TOOL_NAME_PATTERN = Pattern.compile(
            "^(?:EXEC|NMAKE|LINK|MSBUILD|CSC|MSBuild|link|nmake|msbuild|cl|rs)$|"
            + "^[^/\\\\]*\\.exe$|"
            + "^<[^>]+>$",
            Pattern.CASE_INSENSITIVE);

    /**
     * Pattern to identify compiler/linker parameters (e.g., /INCREMENTAL, /OPT:ICF, -Wall).
     * These should not be treated as filenames.
     */
    private static final Pattern LINKER_PARAMETER_PATTERN = Pattern.compile(
            "^[/-][A-Z][A-Z0-9]*(?::[A-Z0-9]+)?$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern LINKER_CAUSE = Pattern.compile(".*imported by '([A-Za-z0-9\\-_.]+)'.*");
    private static final String EXPECTED_CATEGORY = "Expected";
    private static final String MSBUILD = "MSBUILD";
    private static final String NO_SOURCE_FILE = "-";
    private static final Pattern HEADER_COMPILE_MESSAGE = Pattern.compile("\\(compiling source file .*\\)");

    // Pattern to extract Delphi file path from message (e.g., "C:\Path\File.pas(123) Warning: ...")
    private static final Pattern DELPHI_FILE_PATTERN = Pattern.compile(
            "^\\s*([^:]+\\.(?:pas|dpr|dpk|dproj))\\((\\d+)\\)\\s+(?:[Ww]arning|[Hh]int)\\s*:\\s*([A-Za-z0-9]+)\\s+(.*)$");

    private static final Pattern SYSTEM_TOOL = Pattern.compile(".*\\.exe$|^<[^>]+>$", Pattern.CASE_INSENSITIVE);
    private static final Pattern INVALID_PATH_CHARS = Pattern.compile("[<>|\"?]");

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

        if (isLinkerParameter(fileName)) {
            fileName = NO_SOURCE_FILE;
        }
        // Skip if this is a tool name (executable or tool without proper source extension)
        else {
            var checkResult = checkToolName(fileName);
            if (checkResult.isEmpty()) {
                return Optional.empty();
            }
            fileName = checkResult.get();
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

    private Optional<Issue> createDelphiEmbeddedIssue(final Matcher matcher, @CheckForNull final String message,
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

    private Optional<Issue> createStandardIssue(final Matcher matcher, @CheckForNull final String message,
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
     * Checks if the given fileName is a compiler/linker parameter (e.g., /INCREMENTAL, /OPT:ICF).
     *
     * @param fileName
     *         the filename to check
     *
     * @return true if this is a linker parameter, false otherwise
     */
    private boolean isLinkerParameter(final String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return false;
        }
        String trimmedFileName = fileName.trim();
        return LINKER_PARAMETER_PATTERN.matcher(trimmedFileName).matches();
    }

    /**
     * Checks if the given fileName is a known tool name.
     * Tool names include executables (e.g., ConsoleTranslator.exe) and bare tool names (e.g., NMAKE, rs).
     * This method is conservative and only filters known tool names to avoid false positives.
     *
     * @param fileName
     *         the filename to check
     *
     * @return the resolved filename if it should be kept, or empty if it should be dropped
     */
    private Optional<String> checkToolName(@CheckForNull final String fileName) {
        if (StringUtils.isBlank(fileName) || NO_SOURCE_FILE.equals(fileName) || "unknown.file".equals(fileName)) {
            return Optional.empty();
        }

        var baseFileName = FilenameUtils.getName(fileName);
        var cleanFileName = baseFileName.trim().replaceAll("^\\d{1,2}:\\d{2}:\\d{2}\\s+", "");

        if (TOOL_NAME_PATTERN.matcher(cleanFileName).matches()) {
            return SYSTEM_TOOL.matcher(cleanFileName).matches() ? Optional.empty() : Optional.of(NO_SOURCE_FILE);
        }

        return Optional.of(fileName);
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
        var fileName = extractFileNameFromGroups(matcher);
        fileName = resolveFileName(fileName, matcher);
        return normalizeFileName(fileName, matcher.group(23));
    }

    private String extractFileNameFromGroups(final Matcher matcher) {
        // Group 3 is from COMMAND_LINE_WARNING_PATTERN
        return StringUtils.firstNonBlank(matcher.group(3), matcher.group(18), matcher.group(9));
    }

    private String resolveFileName(@CheckForNull final String fileName, final Matcher matcher) {
        if (StringUtils.isNotBlank(fileName)) {
            return fileName;
        }

        var messageText = matcher.group(22);
        var message = messageText == null ? "" : messageText;
        // Group 22 is message
        var linker = LINKER_CAUSE.matcher(message);
        return linker.matches() ? linker.group(1)
                : StringUtils.defaultIfBlank(StringUtils.substringBetween(message, "'"), "unknown.file");
    }

    private String normalizeFileName(final String fileName, @CheckForNull final String projectDir) {
        var concatenated = FilenameUtils.concat(projectDir, fileName);
        var normalized = canResolveRelativeFileName(fileName, projectDir)
                ? (concatenated != null ? concatenated : fileName) : fileName;

        return (MSBUILD.equals(normalized.trim()) || containsInvalidPathCharacters(normalized))
                ? NO_SOURCE_FILE : normalized;
    }

    private boolean containsInvalidPathCharacters(final String fileName) {
        return INVALID_PATH_CHARS.matcher(fileName).find()
                || (fileName.contains("*") && !fileName.contains("/") && !fileName.contains("\\"));
    }

    private boolean canResolveRelativeFileName(final String fileName, @CheckForNull final String projectDir) {
        if (StringUtils.isBlank(projectDir)) {
            return false;
        }
        return FilenameUtils.getPrefixLength(fileName) == 0
                && !MSBUILD.equals(fileName.trim());
    }
}
