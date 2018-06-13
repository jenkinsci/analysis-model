package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Report;

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
    private String fileName;

    /**
     * Creates a new parser.
     */
    public RfLintParser() {
        super(RFLINT_ERROR_PATTERN);
    }

    @Override
    public Report parse(final Reader file, final Function<String, String> preProcessor) {
        Report warnings = new Report();
        try (LineIterator iterator = IOUtils.lineIterator(file)) {
            Pattern filePattern = Pattern.compile(RFLINT_FILE_PATTERN);
            while (iterator.hasNext()) {
                String line = preProcessor.apply(iterator.nextLine());
                Matcher matcher = filePattern.matcher(line);
                if (matcher.find()) {
                    fileName = matcher.group(1);
                }
                findIssues(line, warnings);
            }
        }
        catch (IOException exception) {
            throw new ParsingException(exception, "Can't read input lines");
        }
        return warnings;
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(4);
        String category = guessCategoryIfEmpty(matcher.group(1), message);
        Priority priority = Priority.LOW;
        switch (category.charAt(0)) {
            case 'E':
                priority = Priority.HIGH;
                category = "ERROR";
                break;
            case 'W':
                priority = Priority.NORMAL;
                category = "WARNING";
                break;
            case 'I':
                priority = Priority.LOW;
                category = "IGNORE";
                break;
            default:
                break;
        }
        return builder.setFileName(fileName)
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(category)
                .setMessage(message)
                .setPriority(priority)
                .build();
    }
}
