package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.Strings;

import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser for Eclipse compiler warnings.
 *
 * @author Ullrich Hafner
 * @author Jason Faust
 */
public class EclipseParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 425883472788422955L;

    private static final String ECLIPSE_FIRST_LINE_REGEXP =
            ".*\\d+\\.\\s*(?<severity>WARNING|ERROR|INFO) in (?<file>.*)\\s*\\(at line (?<line>\\d+)\\)";

    static final String WARNING = "WARNING";
    static final String ERROR = "ERROR";
    static final String INFO = "INFO";

    private static final String JAVADOC_PREFIX = "Javadoc:";
    private static final Pattern ANT_PREFIX = Pattern.compile("^(?:.*\\[.+\\])?\\s*(.*)");

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
    protected boolean isLineInteresting(final String line) {
        return line.contains(WARNING) || line.contains(ERROR) || line.contains(INFO);
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
        var messageMatcher = ANT_PREFIX.matcher(message);
        if (messageMatcher.matches()) {
            var msg = messageMatcher.group(1);
            builder.setMessage(msg);
            extractCategory(builder, msg);
        }
    }

    /**
     * Sets the issue's category to {@code Javadoc} if the message starts with {@value #JAVADOC_PREFIX}, {@code Other}
     * otherwise. Unlike {@link #extractMessage(IssueBuilder, String)}, the {@code message} is assumed to be cleaned-up.
     *
     * @param builder
     *     IssueBuilder to populate.
     * @param message
     *     issue to examine.
     */
    static void extractCategory(final IssueBuilder builder, final String message) {
        if (Strings.CS.startsWith(message, JAVADOC_PREFIX)) {
            builder.setCategory(Categories.JAVADOC);
        }
        else {
            builder.setCategory(Categories.OTHER);
        }
    }
}
