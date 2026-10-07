package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import org.apache.commons.lang3.StringUtils;

/**
 * A parser for CMake warnings. Keeps the source filenames reported by CMake: build-tool working directories and CMake's
 * binary-directory marker do not identify the source directory of a configure diagnostic. Relative filenames remain
 * relative for consumers to resolve using source-tree context.
 *
 * @author Uwe Brandt
 */
public class CMakeParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 8149238560432255036L;

    private static final String CMAKE_WARNING_PATTERN =
            "^(?<prefix>.*?)CMake\\s+(?<type>Warning|Deprecation Warning|Error)(?:.*?(?<file>\\S+)){0,1}(?::(?<line>\\d+)\\s+(?<category>\\S+)){0,1}\\s*:";

    /** Creates a new instance of {@link CMakeParser}. */
    public CMakeParser() {
        super(CMAKE_WARNING_PATTERN);
    }

    @Override
    protected boolean isDirectoryTrackingEnabled() {
        return false;
    }

    @Override
    protected Optional<Issue> createIssue(
            final Matcher matcher, final LookaheadStream lookahead, final IssueBuilder builder) {
        // if the category is contained in brackets, remove those brackets
        var category = StringUtils.strip(matcher.group("category"), "()");
        var prefix = matcher.group("prefix");
        return builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"))
                .setCategory(category)
                .setMessage(readMessage(lookahead, prefix))
                .setSeverity(Severity.guessFromString(matcher.group("type")))
                .buildOptional();
    }

    private String readMessage(final LookaheadStream lookahead, final String prefix) {
        List<String> messageLines = new ArrayList<>();
        while (lookahead.hasNext()) {
            var line = removePrefix(lookahead.peekNext(), prefix);
            if (!isContinuation(line)) {
                break;
            }
            lookahead.next();
            messageLines.add(line.strip());
        }

        while (!messageLines.isEmpty()
                && messageLines.get(messageLines.size() - 1).isEmpty()) {
            messageLines.remove(messageLines.size() - 1);
        }
        return String.join("\n", messageLines);
    }

    private String removePrefix(final String line, final String prefix) {
        if (line.startsWith(prefix)) {
            return line.substring(prefix.length());
        }
        return line.equals(prefix.stripTrailing()) ? "" : line;
    }

    private boolean isContinuation(final String line) {
        return line.isEmpty() || Character.isWhitespace(line.charAt(0));
    }
}
