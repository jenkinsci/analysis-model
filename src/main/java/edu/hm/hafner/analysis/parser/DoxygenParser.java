package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for Doxygen.
 *
 * @author Ladislav Moravek
 */
public class DoxygenParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 8760302999081711502L;

    private static final String DOXYGEN_WARNING_PATTERN =
            ANT_TASK + "(?:(?:(.+?):(\\d+):|(.+?)\\((\\d+)\\):)?(?:(\\d+):)?)? ?([wW]arning|[Ee]rror): (.*)$";

    /**
     * Creates a new instance of {@link DoxygenParser}.
     */
    public DoxygenParser() {
        super(DOXYGEN_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return (line.contains("arning") || line.contains("rror")) && !line.contains("[javac]");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
                                          final IssueBuilder builder) {
        var message = new StringBuilder(matcher.group(7));

        String fileName;
        if ((matcher.group(1) != null) || (matcher.group(2) != null)) {
            fileName = cleanupFileName(matcher.group(1));
            builder.setFileName(fileName);
            builder.setLineStart(matcher.group(2));
        }
        else {
            fileName = cleanupFileName(matcher.group(3));
            builder.setFileName(fileName);
            builder.setLineStart(matcher.group(4));
        }

        while (lookahead.hasNext() && Gcc4CompilerParser.isMessageContinuation(lookahead)) {
            message.append('\n');
            message.append(lookahead.next());
        }

        return builder
                .setColumnStart(matcher.group(5))
                .setMessage(message.toString())
                .setSeverity(Severity.guessFromString(matcher.group(6)))
                .buildOptional();
    }

    /**
     * Cleans up the filename by removing Doxygen progress messages that may have been captured.
     * For example, "Generating caller graph for function vCanOpenDevD:/path/file.c" becomes "D:/path/file.c".
     *
     * @param fileName the raw filename from the regex match
     * @return the cleaned filename
     */
    private String cleanupFileName(final String fileName) {
        if (fileName == null || !fileName.contains("Generating")) {
            return fileName;
        }

        // Try to extract Windows absolute path (e.g., D:/path or D:\path)
        String extracted = extractWindowsPath(fileName);
        if (extracted != null) {
            return extracted;
        }

        // Try to extract Unix absolute path (starting with /)
        return extractUnixPath(fileName);
    }

    @CheckForNull
    private String extractWindowsPath(final String fileName) {
        int windowsPathIndex = fileName.indexOf(":/");
        if (windowsPathIndex == -1) {
            windowsPathIndex = fileName.indexOf(":\\");
        }

        if (windowsPathIndex > 0 && windowsPathIndex < fileName.length() - 1) {
            char driveLetter = fileName.charAt(windowsPathIndex - 1);
            if (Character.isLetter(driveLetter)) {
                return fileName.substring(windowsPathIndex - 1);
            }
        }
        return null;
    }

    private String extractUnixPath(final String fileName) {
        int generatingIndex = fileName.indexOf("Generating");
        if (generatingIndex < 0) {
            return fileName;
        }

        int pathStart = fileName.indexOf('/', generatingIndex);
        int unixPathIndex = fileName.lastIndexOf('/');

        if (pathStart > 0 && pathStart < unixPathIndex) {
            return fileName.substring(pathStart);
        }
        return fileName;
    }
}