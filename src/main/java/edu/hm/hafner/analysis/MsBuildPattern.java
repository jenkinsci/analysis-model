package edu.hm.hafner.analysis;

/**
 * Contains the patterns for the MsBuildParser {@link edu.hm.hafner.analysis.parser.MsBuildParser MsBuildParser}.
 */
public class MsBuildPattern {
    public static final String COMMAND_LINE_WARNING_PATTERN =
            "(?:^(?:.*)Command line warning ([A-Za-z0-9]+):\\s*(.*)\\s*\\[(.*)\\])"; // Group 1 - 3

    public static final String OPTIONAL_LINE_NUMBER_PATTERN =
            "(?:\\s*(?:\\d+|\\d+:\\d+)>)?"; // Optional Group

    private static final String LINE_COLUMN_PATTERN =
            "\\((?:(\\d+),(\\d+),(\\d+),(\\d+)|(\\d+)(?:-(\\d+))?(?:,(\\d+)(?:-(\\d+))?)?)\\)"; // Group 5 - 12

    public static final String FILE_NAME_PATTERN =
            "(?:(?:(?:(.*?)" // Group 4
            + LINE_COLUMN_PATTERN
            + "|.*LINK)\\s*:|(.*):)"; // Group 13

    public static final String SEVERITY_PATTERN =
            "\\s*([A-z-_]*\\s(?:[Nn]ote|[Ii]nfo|[Ww]arning|(?:fatal\\s*)?[Ee]rror))[^A-Za-z0-9]\\s*:?\\s*"; // Group 14

    public static final String CATEGORY_PATTERN =
            "([A-Za-z0-9\\-_]+)?\\s*:\\s"; // Group 15

    public static final String TYPE_PATTERN =
            "(?:\\s*([A-Za-z0-9.]+)\\s*:)?\\s*"; // Group 16

    public static final String MESSAGE_PATTERN =
            "(.*?)"; // Group 17

    public static final String PROJECT_DIR_PATTERN =
            "(?: \\[([^\\]]*)[/\\\\][^\\]\\\\]+\\])?"; // Group 18
}
