package edu.hm.hafner.analysis;

/**
 * Contains the patterns for the MsBuildParser {@link edu.hm.hafner.analysis.parser.MsBuildParser MsBuildParser}.
 */
@SuppressWarnings("PMD.DataClass")
public class MsBuildPattern {
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
}
