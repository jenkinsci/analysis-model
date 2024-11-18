package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.analysis.parser.EclipseParser.*;

/**
 * A parser for Eclipse compiler warnings.
 *
 * @author Ullrich Hafner
 * @author Jason Faust
 */
public class EclipseMavenParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 425883472788422955L;

    private static final String ECLIPSE_FIRST_LINE_REGEXP =
            "\\s*\\[(?<severity>WARNING|ERROR|INFO)\\]\\s*(?<file>.*):\\[(?<line>\\d+)(?:,\\d+)?\\]\\s*(?<message>.*)";

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return !isXmlFile(readerFactory);
    }

    /**
     * Creates a new instance of {@link EclipseMavenParser}.
     */
    public EclipseMavenParser() {
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

        String message = matcher.group("message");
        if (StringUtils.isNotBlank(message)) { // single line format
            builder.setMessage(message);
            extractCategory(builder, message);
        }
        else { // multi line format
            List<String> code = new ArrayList<>();
            while (lookahead.hasNext("^\\t.*$") && lookahead.hasNext()) {
                code.add(lookahead.next());
            }
            builder.setAdditionalProperties(code.hashCode());

            if (lookahead.hasNext()) {
                extractMessage(builder, RegExUtils.removeFirst(lookahead.next(), ".*\\t"));
            }
        }

        return builder.buildOptional();
    }
}
