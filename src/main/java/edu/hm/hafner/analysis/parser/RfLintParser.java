package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import static edu.hm.hafner.analysis.Categories.*;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for <a href="http://robotframework.org/">Robot Framework</a> Parse output from <a
 * href="https://github.com/boakley/robotframework-lint">robotframework-lint</a>. To generate rflint file cmd$ pip
 * install robotframework-lint cmd$ rflint path/to/test.robot
 *
 * @author traitanit
 */
public class RfLintParser extends RegexpLineParser {
    private static final long serialVersionUID = -7903991158616386226L;

    private static final String RFLINT_ERROR_PATTERN = "([W|E|I]): (\\d+), (\\d+): (.*) \\((.*)\\)";
    private static final String RFLINT_FILE_PATTERN = "\\+\\s(.*)";
    private String fileName = StringUtils.EMPTY;

    /**
     * Creates a new parser.
     */
    public RfLintParser() {
        super(RFLINT_ERROR_PATTERN);
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) {
        try (Stream<String> lines = readerFactory.readStream()) {
            Report warnings = new Report();
            Pattern filePattern = Pattern.compile(RFLINT_FILE_PATTERN);
            lines.forEach(line -> {
                Matcher matcher = filePattern.matcher(line);
                if (matcher.find()) {
                    fileName = matcher.group(1);
                }
                findIssues(line, warnings);
            });
            return warnings;
        }
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(4);
        String category = guessCategoryIfEmpty(matcher.group(1), message);
        Severity priority = Severity.WARNING_LOW;
        switch (category.charAt(0)) {
            case 'E':
                priority = Severity.WARNING_HIGH;
                category = "ERROR";
                break;
            case 'W':
                priority = Severity.WARNING_NORMAL;
                category = "WARNING";
                break;
            case 'I':
                priority = Severity.WARNING_LOW;
                category = "IGNORE";
                break;
            default:
                break;
        }
        return builder.setFileName(fileName)
                .setLineStart(matcher.group(2))
                .setCategory(category)
                .setMessage(message)
                .setSeverity(priority)
                .buildOptional();
    }
}
