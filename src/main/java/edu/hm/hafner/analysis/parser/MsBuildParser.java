package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the MSBuild/PcLint compiler warnings.
 *
 * @author Ullrich Hafner
 */
public class MsBuildParser extends RegexpLineParser {
    private static final long serialVersionUID = -2141974437420906595L;
    private static final String MS_BUILD_WARNING_PATTERN = "(?:^(?:.*)Command line warning ([A-Za-z0-9]+):\\s*(.*)" +
            "\\s*\\[(.*)\\])|" + ANT_TASK + "(?:(?:\\s*\\d+>)?(?:(?:(?:(.*)\\((\\d*)(?:,(\\d+))?.*\\)|.*LINK)\\s*:|(" +
            ".*):)\\s*([A-z-_]*\\s?(?:[Nn]ote|[Ii]nfo|[Ww]arning|(?:fatal\\s*)?[Ee]rror))\\s*:?\\s*([A-Za-z0-9]+)" +
            "\\s*:\\s(?:\\s*([A-Za-z0-9.]+)\\s*:)?\\s*(.*?)(?: \\[([^\\]]*)[/\\\\][^\\]\\\\]+\\])?" + "|(.*)\\s*:" +
            ".*error\\s*(LNK[0-9]+):\\s*(.*)))$";

    /**
     * Creates a new instance of {@link MsBuildParser}.
     */
    public MsBuildParser() {
        super("msbuild", MS_BUILD_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher, final IssueBuilder builder) {
        String fileName = determineFileName(matcher);
        if (StringUtils.isNotBlank(matcher.group(2))) {
            return builder.setFileName(fileName).setLineStart(0).setCategory(matcher.group(1))
                                 .setMessage(matcher.group(2)).setPriority(Priority.NORMAL).build();
        }
        else if (StringUtils.isNotBlank(matcher.group(13))) {
            return builder.setFileName(fileName).setLineStart(0).setCategory(matcher.group(14))
                                 .setMessage(matcher.group(15)).setPriority(Priority.HIGH).build();
        }
        else {
            Issue warning;
            if (StringUtils.isNotEmpty(matcher.group(10))) {
                warning = builder.setFileName(fileName).setLineStart(parseInt(matcher.group(5)))
                                        .setColumnStart(parseInt(matcher.group(6))).setCategory(matcher.group(9))
                                        .setType(matcher.group(10)).setMessage(matcher.group(11))
                                        .setPriority(determinePriority(matcher)).build();
            }
            else {
                String category = matcher.group(9);
                if ("Expected".matches(category)) {
                    return FALSE_POSITIVE;
                }
                warning = builder.setFileName(fileName).setLineStart(parseInt(matcher.group(5)))
                                        .setColumnStart(parseInt(matcher.group(6))).setCategory(category)
                                        .setMessage(matcher.group(11)).setPriority(determinePriority(matcher)).build();
            }
            return warning;
        }
    }

    /**
     * Determines the name of the file that is cause of the warning.
     *
     * @param matcher the matcher to get the matches from
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

        final String projectDir = matcher.group(12);
        if (StringUtils.isNotBlank(projectDir) && FilenameUtils.getPrefixLength(fileName) == 0 && !fileName.trim()
                                                                                                           .equals("MSBUILD")) {
            // resolve fileName relative to projectDir
            fileName = FilenameUtils.concat(projectDir, fileName);
        }
        return fileName;
    }

    /**
     * Determines the priority of the warning.
     *
     * @param matcher the matcher to get the matches from
     * @return the priority of the warning
     */
    private Priority determinePriority(final Matcher matcher) {
        if (isOfType(matcher, "note") || isOfType(matcher, "info")) {
            return Priority.LOW;
        }
        else if (isOfType(matcher, "warning")) {
            return Priority.NORMAL;
        }
        return Priority.HIGH;
    }

    /**
     * Returns whether the warning type is of the specified type.
     *
     * @param matcher the matcher
     * @param type    the type to match with
     * @return <code>true</code> if the warning type is of the specified type
     */
    private boolean isOfType(final Matcher matcher, final String type) {
        return StringUtils.containsIgnoreCase(matcher.group(8), type);
    }
}

