package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static j2html.TagCreator.*;

/**
 * A parser for ErrorProne warnings during a Maven build.
 *
 * @author Ullrich Hafner
 */
public class ErrorProneParser extends IssueParser {
    private static final Pattern URL_PATTERN = Pattern.compile("\\s+\\(see (?<url>http\\S+)\\s*\\)");
    private static final Pattern FIX_PATTERN = Pattern.compile("\\s+Did you mean '(?<code>.*)'\\?");
    private static final Pattern WARNING_PATTERN
            = Pattern.compile("\\[(?<severity>WARNING|ERROR)\\]\\s+"
            + "(?<file>.+):"
            + "\\[(?<line>\\d+),(?<column>\\d+)\\]\\s+"
            + "\\[(?<type>\\w+)\\]\\s+"
            + "(?<message>.*)");

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        Report report = new Report();
        try (Stream<String> lines = readerFactory.readStream()) {
            LookaheadStream lookahead = new LookaheadStream(lines);
            while (lookahead.hasNext()) {
                String line = lookahead.next();
                Matcher matcher = WARNING_PATTERN.matcher(line);
                if (matcher.matches()) {
                    report.add(findIssues(matcher, lookahead));
                }
            }
        }

        return report;
    }

    private Issue findIssues(final Matcher matcher, final LookaheadStream lookahead) {
        IssueBuilder builder = new IssueBuilder();
        builder.setFileName(matcher.group("file"))
                .setLineStart(matcher.group("line"))
                .setColumnStart(matcher.group("column"))
                .setType(matcher.group("type"))
                .setMessage(matcher.group("message"));
        if ("ERROR".equals(matcher.group("severity"))) {
            builder.setSeverity(Severity.ERROR);
        }
        else {
            builder.setSeverity(Severity.WARNING_NORMAL);
        }
        StringBuilder description = new StringBuilder();
        StringBuilder url = new StringBuilder();
        while (lookahead.hasNext("\\s+.*")) {
            String line = lookahead.next();
            Matcher urlMatcher = URL_PATTERN.matcher(line);
            if (urlMatcher.matches()) {
                url.append(p().with(
                        a().withHref(urlMatcher.group("url"))
                        .withText("See ErrorProne documentation."))
                        .render());
            }
            else {
                Matcher fixMatcher = FIX_PATTERN.matcher(line);
                if (fixMatcher.matches()) {
                    description.append("Did you mean: ");
                    description.append(pre().with(
                            code().withText(fixMatcher.group("code"))).render());
                }
            }
        }

        return builder.setDescription(description.toString() + url.toString()).build();
    }
}
