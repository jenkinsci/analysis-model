package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the PyLint compiler warnings.
 *
 * @author Sebastian Hansbauer
 */
public class PyLintParser extends FastRegexpLineParser {
    private static final long serialVersionUID = 4464053085862883240L;

    private static final String PYLINT_ERROR_PATTERN = "(.*):(\\d+): \\[(\\D\\d*).*\\] (.*)";

    /**
     * Creates a new instance of {@link PyLintParser}.
     */
    public PyLintParser() {
        super(PYLINT_ERROR_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[");
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String message = matcher.group(4);
        String category = guessCategoryIfEmpty(matcher.group(3), message);

        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(category)
                .setMessage(message)
                .setSeverity(mapPriorty(category))
                .build();
    }

    private Severity mapPriorty(final String category) {
        //First letter of the Pylint classification is one of F/E/W/R/C. E/F/W are high priority.
        Severity priority;

        // See http://docs.pylint.org/output.html for definitions of the categories
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
