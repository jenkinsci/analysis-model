package edu.hm.hafner.analysis;

import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.LookaheadStream;

/**
 * Parses a report file line by line for issues using a pre-defined regular expression. If the regular expression
 * matches then the abstract method {@link #createIssue(Matcher, LookaheadStream, IssueBuilder)} will be called. Sub
 * classes need to provide an implementation that transforms the {@link Matcher} instance into a new issue. If required,
 * sub classes may consume additional lines from the report file before control is handed back to the template method of
 * this parser.
 *
 * @author Ullrich Hafner
 */
public abstract class LookaheadParser extends IssueParser {
    private static final long serialVersionUID = 3240719494150024894L;

    /** Pattern identifying an ant task debug output prefix. */
    protected static final String ANT_TASK = "^(?:.*\\[[^]]*\\])?\\s*";

    private static final String ENTERING_DIRECTORY = "Entering directory";
    private static final String LEAVING_DIRECTORY = "Leaving directory";
    private static final Pattern ENTERING_DIRECTORY_PATH
            = Pattern.compile(".*" + ENTERING_DIRECTORY + " (?<dir>.*)");
    private static final String CMAKE_PREFIX = "-- Build files have";
    private static final Pattern CMAKE_PATH = Pattern.compile(".*" + CMAKE_PREFIX + " been written to: (?<dir>.*)");
    private static final String HYPHEN = "'`";

    private static final int MAX_LINE_LENGTH = 4000; // see JENKINS-55805

    private final Pattern pattern;

    private final Stack<String> recursiveDirectories;

    /**
     * Creates a new instance of {@link LookaheadParser}.
     *
     * @param pattern
     *         pattern of compiler warnings.
     */
    protected LookaheadParser(final String pattern) {
        super();

        this.pattern = Pattern.compile(pattern);
        this.recursiveDirectories = new Stack<>();
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        Report report = new Report();
        try (Stream<String> lines = readerFactory.readStream()) {
            try (LookaheadStream lookahead = new LookaheadStream(lines, readerFactory.getFileName())) {
                parse(report, lookahead);
            }
        }

        return postProcess(report);
    }

    private void parse(final Report report, final LookaheadStream lookahead) {
        try (IssueBuilder builder = new IssueBuilder()) {
            while (lookahead.hasNext()) {
                String line = lookahead.next();
                handleDirectoryChanges(builder, line);
                preprocessLine(line);
                if (isLineInteresting(line)) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        createIssue(matcher, lookahead, builder).ifPresent(report::add);
                    }
                }
                if (Thread.interrupted()) {
                    throw new ParsingCanceledException();
                }
            }
        }
    }

    /**
     * Preprocesses the specified line. This method is called before the line is checked for a match. Subclasses may
     * override this empty default implementation.
     *
     * @param line
     *         the line to preprocess
     */
    protected void preprocessLine(final String line) {
        // empty default implementation does nothing
    }

    /**
     * When changing directories using 'Entering directory' output, save new directory to our stack for later use, then
     * return it for use now.
     *
     * @param line
     *         the line to parse
     * *
     * @return The new directory to change to
     */
    private String enterDirectory(final String line) {
        recursiveDirectories.push(extractDirectory(line, ENTERING_DIRECTORY_PATH));
        return recursiveDirectories.peek();
    }

    /**
     * When changing directories using 'Leaving directory' output, set our stack to the last directory seen, and return
     * that directory.
     *
     * @return The last directory seen, or an empty String if we have returned to the beginning
     */
    private String leaveDirectory() {
        if (!recursiveDirectories.empty()) {
            recursiveDirectories.pop();
            if (!recursiveDirectories.empty()) {
                return recursiveDirectories.peek();
            }
        }
        return "";
    }

    /**
     * Uses Make-like ("Entering directory" and "Leaving directory") and CMake-like ("Build files have been written to")
     * output to track directory structure as the compiler moves between source locations.
     *
     * @param builder
     *         {@link IssueBuilder} to set directory for
     * @param line
     *         the line to parse
     */
    private void handleDirectoryChanges(final IssueBuilder builder, final String line) {
        if (line.contains(ENTERING_DIRECTORY)) {
            builder.setDirectory(enterDirectory(line));
        }
        else if (line.contains(LEAVING_DIRECTORY)) {
            builder.setDirectory(leaveDirectory());
        }
        else if (line.contains(CMAKE_PREFIX)) {
            builder.setDirectory(extractDirectory(line, CMAKE_PATH));
        }
    }

    /**
     * Extracts a directory from a line using a specified pattern which contains a capture group named 'dir'.
     *
     * @param line
     *         the line to parse using makePath
     * @param makePath
     *         {@link Pattern} which includes a capture group name 'dir'
     *
     * @return A path extracted from the input line
     * @throws IllegalArgumentException
     *         If the {@link Pattern} does not contain a capture group named 'dir'
     * @throws ParsingException
     *         If the {@link Pattern} fails to match the input line
     */
    private String extractDirectory(final String line, final Pattern makePath) throws ParsingException {
        if (!makePath.toString().contains("<dir>")) {
            throw new IllegalArgumentException(
                    String.format("%s does not contain a capture group named 'dir'", makePath));
        }
        Matcher makeLineMatcher = makePath.matcher(line);
        if (makeLineMatcher.matches()) {
            return removeHyphen(makeLineMatcher.group("dir"));
        }
        throw new ParsingException(
                "Unable to change directory using: %s to match %s", makePath.toString(), line);
    }

    /**
     * Creates a new issue for the specified pattern. This method is called for each matching line in the specified
     * file. If a match is a false positive, then return {@link Optional#empty()} to ignore this warning.
     *
     * @param matcher
     *         the regular expression matcher
     * @param lookahead
     *         the lookahead stream to read additional lines
     * @param builder
     *         the issue builder to use
     *
     * @return a new annotation for the specified pattern
     * @throws ParsingException
     *         Signals that during parsing a non recoverable error has been occurred
     */
    protected abstract Optional<Issue> createIssue(Matcher matcher, LookaheadStream lookahead, IssueBuilder builder)
            throws ParsingException;

    /**
     * Returns whether the specified line is interesting. Each interesting line will be matched by the defined regular
     * expression. Here a parser can implement some fast checks (i.e. string or character comparisons) in order to see
     * if a required condition is met. This default implementation does always return {@code true}.
     *
     * @param line
     *         the line to inspect
     *
     * @return {@code true} if the line should be handed over to the regular expression scanner, {@code false} if the
     *         line does not contain a warning.
     */
    protected boolean isLineInteresting(final String line) {
        return line.length() < MAX_LINE_LENGTH; // skip long lines, see JENKINS-55805
    }

    /**
     * Post processes the issues. This default implementation does nothing.
     *
     * @param report
     *         the issues after the parsing process
     *
     * @return the post processed issues
     */
    protected Report postProcess(final Report report) {
        return report;
    }

    /**
     * Remove Hyphen from directory path if it starts or ends with hyphen.
     *
     * @param dir
     *         directory path to inspect
     *
     * @return directory path without leading or trailing hyphen
     */
    private String removeHyphen(final String dir) {
        String path = dir;
        path = StringUtils.stripStart(path, HYPHEN);
        path = StringUtils.stripEnd(path, HYPHEN);
        return path;
    }
}
