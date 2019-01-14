package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.umd.cs.findbugs.annotations.CheckForNull;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

import static edu.hm.hafner.analysis.Categories.*;

/**
 * A parser for the PyLint compiler warnings.
 *
 * @author Sebastian Hansbauer
 * @author Björn Pedersen
 */
public class PyLintParser extends RegexpLineParser {
    private static final long serialVersionUID = 4464053085862883240L;

    // the default pattern matches "--output-format=parseable" output.
    private static final String DEFAULT_PYLINT_ERROR_PATTERN = "(?<path>.*):(?<line>\\d+): \\[(?<category>\\D\\d*)\\((?<symbol>.*)\\), .*?\\] (?<message>.*)";

    private static final String UNKNOWN_CAT = "pylint-unknown";

    /**
     * Creates a new instance of {@link PyLintParser}.
     */
    public PyLintParser() {
        super(DEFAULT_PYLINT_ERROR_PATTERN);
    }

    public PyLintParser(@CheckForNull String regExp) {
        super(regExp);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group("message");
        String category;
        try {
            category = matcher.group("symbol");
            if (StringUtils.isEmpty(category)) {
                category = UNKNOWN_CAT;
            }
        } catch (IllegalArgumentException e) {
            // if no symbolic name is available, use the numeric one
            category = matcher.group("category");
        }
        Severity priority = mapPriority(matcher.group("category"));

        return builder.setFileName(matcher.group("path")).setLineStart(matcher.group("line")).setCategory(category)
                .setMessage(message).setSeverity(priority).buildOptional();
    }

    private Severity mapPriority(final String category) {
        // First letter of the Pylint classification is one of F/E/W/R/C. E/F/W are high
        // priority.
        Severity priority;

        // See http://docs.pylint.org/output.html for definitions of the categories
        if (category.isEmpty()) {
            // if the category is missing from the output, default to 'normal'.
            return Severity.WARNING_NORMAL;
        }
        switch (category.charAt(0)) {
        // [R]efactor for a ?good practice? metric violation
        // [C]onvention for coding standard violation
        case 'R':
        case 'C':
            priority = Severity.WARNING_LOW;
            break;

        // [W]arning for stylistic problems, or minor programming issues
        case 'W':
            priority = Severity.WARNING_NORMAL;
            break;

        // [E]rror for important programming issues (i.e. most probably bug)
        // [F]atal for errors which prevented further processing
        case 'E':
        case 'F':
            priority = Severity.WARNING_HIGH;
            break;

        default:
            priority = Severity.WARNING_LOW;
            break;
        }
        return priority;
    }
}
