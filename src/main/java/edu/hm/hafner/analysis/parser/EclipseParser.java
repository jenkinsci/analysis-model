package edu.hm.hafner.analysis.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for Eclipse compiler warnings.
 *
 * @author Ullrich Hafner
 * @author Jason Faust
 */
public class EclipseParser extends LookaheadParser {
    private static final long serialVersionUID = 425883472788422955L;

    private static final String ECLIPSE_FIRST_LINE_REGEXP =
            ".*\\d+\\.\\s*(?<severity>WARNING|ERROR|INFO) in (?<file>.*)\\s*\\(at line (?<line>\\d+)\\)";

    static final String WARNING = "WARNING";
    static final String ERROR = "ERROR";
    static final String INFO = "INFO";

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return !isXmlFile(readerFactory);
    }

    /**
     * Creates a new instance of {@link EclipseParser}.
     */
    public EclipseParser() {
        super(ECLIPSE_FIRST_LINE_REGEXP);
    }

    @Override
    protected String interestingLineContent(String line) {
        if (line.contains(WARNING)
                || line.contains(ERROR)
                || line.contains(INFO)) {
            return line;
        }

        return null;
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        builder.guessSeverity(matcher.group("severity"))
                .setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"));

        List<String> context = new ArrayList<>();
        while (!lookahead.hasNext("^.*----------.*$") && lookahead.hasNext()) {
            context.add(lookahead.next());
        }

        if (!context.isEmpty()) {
            extractMessage(builder, context.remove(context.size() - 1));
        }
        builder.setAdditionalProperties(context.hashCode());

        return builder.buildOptional();
    }

    static void extractMessage(final IssueBuilder builder, final String message) {
        Pattern ant = Pattern.compile("^(?:.*\\[.*\\])?\\s*(.*)");
        Matcher messageMatcher = ant.matcher(message);
        if (messageMatcher.matches()) {
            builder.setMessage(messageMatcher.group(1));
        }
    }
}

