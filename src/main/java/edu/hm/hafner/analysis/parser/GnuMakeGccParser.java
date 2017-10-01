package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the GNU Make and Gcc4 compiler warnings. Read GNU Make output to know where compilation are run.
 *
 * @author vichak
 */
public class GnuMakeGccParser extends RegexpLineParser {
    private static final String SLASH = "/";
    private static final long serialVersionUID = -67701741403245309L;
    private static final String ERROR = "error";

    static final String GCC_ERROR = "GCC error";
    static final String LINKER_ERROR = "Linker error";

    private static final String GNUMAKEGCC_WARNING_PATTERN = "^(" + "(?:.*\\[.*\\])?\\s*" // ANT_TASK
            + "(.*\\.[chpimxsola0-9]+):(\\d+):(?:\\d+:)? (warning|error): (.*)$" // GCC 4 warning
            + ")|(" + "(^g?make(\\[.*\\])?: Entering directory)\\s*(['`]((.*))\\')" // handle make entering directory
            + ")";
    private String directory = "";

    private boolean isWindows;

    /**
     * Creates a new instance of {@link GnuMakeGccParser}.
     */
    public GnuMakeGccParser() {
        this(System.getProperty("os.name"));
    }

    /**
     * Creates a new instance of {@link GnuMakeGccParser} assuming the operating system given in os
     *
     * @param os A string representing the operating system - mainly used for faking
     */
    public GnuMakeGccParser(final String os) {
        super("gmake-gcc", GNUMAKEGCC_WARNING_PATTERN);
        isWindows = os.toLowerCase().contains("windows");
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        if (matcher.group(1) == null) {
            return handleDirectory(matcher);
        }
        else {
            return handleWarning(matcher);
        }
    }

    private Issue handleWarning(final Matcher matcher) {
        String fileName = matcher.group(2);
        int lineNumber = parseInt(matcher.group(3));
        String message = matcher.group(5);
        Priority priority;
        String category;
        if (ERROR.equalsIgnoreCase(matcher.group(4))) {
            priority = Priority.HIGH;
            category = "Error";
        }
        else {
            priority = Priority.NORMAL;
            category = "Warning";
        }
        if (fileName.startsWith(SLASH)) {
            return issueBuilder().setFileName(fileName).setLineStart(lineNumber).setCategory(category)
                                 .setMessage(message).setPriority(priority).build();
        }
        else {
            return issueBuilder().setFileName(directory + fileName).setLineStart(lineNumber).setCategory(category)
                                 .setMessage(message).setPriority(priority).build();
        }
    }

    private String fixMsysTypeDirectory(String directory) {
        if (isWindows && directory.matches("/[a-z]/.*")) {
            //MSYS make on Windows replaces the drive letter and colon (C:) with unix-type absolute paths (/c/)
            //Reverse this operation here
            directory = directory.substring(1, 2) + ":" + directory.substring(2);
        }
        return directory;
    }

    private Issue handleDirectory(final Matcher matcher) {
        directory = matcher.group(10) + SLASH;
        directory = fixMsysTypeDirectory(directory);

        return FALSE_POSITIVE;
    }
}
