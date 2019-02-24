package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.RegexpLineParser;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the PyLint compiler warnings.
 *
 * @author Björn Pedersen
 */
public class PyLintParser extends RegexpLineParser {
    private static final long serialVersionUID = 4464053085862883240L;

    // the default pattern matches "--output-format=parseable" output.
    private static final String PYLINT_PATTERN = "(?<path>[^:]*)(?:\\:(?<module>.*))?:(?<line>\\d+): \\[(?<category>\\D\\d*)(?:\\((?<symbol>.*)\\), )?.*?\\] (?<message>.*)";

    private static final String UNKNOWN_CAT = "pylint-unknown";

    /**
     * Creates a new instance of {@link PyLintParser}.
     */
    public PyLintParser() {
        super(PYLINT_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String category = matcher.group("category");
        builder.setSeverity(mapPriority(category));
        builder.setCategory(StringUtils.firstNonBlank(matcher.group("symbol"), category, UNKNOWN_CAT));

        final String moduleName = matcher.group("module");
        if (moduleName != null) {
            if (moduleName.contains(".")) {
                builder.setPackageName(moduleName.substring(0, moduleName.lastIndexOf(".")));
            }
            builder.setModuleName(moduleName);
        }

        return builder.setFileName(matcher.group("path"))
                .setLineStart(matcher.group("line"))
                .setMessage(matcher.group("message"))
                .buildOptional();
    }

    private Severity mapPriority(final String category) {
        // First letter of the Pylint classification is one of F/E/W/R/C. E/F/W are high
        // priority.

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
                return Severity.WARNING_LOW;

            // [W]arning for stylistic problems, or minor programming issues
            case 'W':
                return Severity.WARNING_NORMAL;

            // [E]rror for important programming issues (i.e. most probably bug)
            // [F]atal for errors which prevented further processing
            case 'E':
            case 'F':
                return Severity.WARNING_HIGH;

            default:
                return Severity.WARNING_LOW;
        }
    }
}
