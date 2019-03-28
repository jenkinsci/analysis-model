package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for CMake warnings.
 *
 * @author Uwe Brandt
 */
public class CMakeParser extends LookaheadParser {
    private static final long serialVersionUID = 8149238560432255036L;

    private static final String CMAKE_WARNING_PATTERN =
            "CMake\\s+Warning(?:.*?(?<file>\\S+)){0,1}(?::(?<line>\\d+)\\s+(?<category>\\S+)){0,1}\\s*:";

    /**
     * Creates a new instance of {@link CMakeParser}.
     */
    public CMakeParser() {
        super(CMAKE_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
                                          final IssueBuilder builder) {
        String message = "";
        if (lookahead.hasNext()) {
            message = lookahead.next();
        }
        // if the category is contained in brackets, remove those brackets
        String category = StringUtils.strip(matcher.group("category"), "()");
        return builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"))
                .setCategory(category)
                .setMessage(message)
                .setSeverity(Severity.WARNING_NORMAL)
                .buildOptional();
    }
}

