package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the PyLint compiler warnings.
 *
 * @author Björn Pedersen
 */
public class PyLintParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 4464053085862883240L;

    // the default pattern matches "--output-format=parseable" output.
    private static final String PYLINT_PATTERN = "(?<path>(?:[A-Z]:\\\\)?[^:]*)(?:\\:(?<module>.*))?:(?<line>\\d+): \\[(?<type>(?<category>[A-Z])\\d*)?(?:\\((?<symbol>.*)\\), )?.*?\\] (?<message>.*)";

    private static final String UNKNOWN_CAT = "pylint-unknown-category";
    private static final String UNKNOWN_TYPE = "pylint-unknown-type";

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
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        var category = matcher.group("category");
        builder.setSeverity(mapPriority(category));
        builder.setCategory(mapCategory(category));
        builder.setType(StringUtils.firstNonBlank(matcher.group("symbol"), matcher.group("type"), UNKNOWN_TYPE));

        var moduleName = matcher.group("module");
        if (moduleName == null) {
            builder.setPackageName("-").setModuleName("-");
        }
        else {
            if (moduleName.contains(".")) {
                builder.setPackageName(moduleName.substring(0, moduleName.lastIndexOf('.')));
            }
            else {
                builder.setPackageName("-");
            }
            builder.setModuleName(moduleName);
        }

        return builder.setFileName(matcher.group("path"))
                .setLineStart(matcher.group("line"))
                .setMessage(matcher.group("message"))
                .buildOptional();
    }

    private String mapCategory(final String category) {
        if (StringUtils.isEmpty(category)) {
            return UNKNOWN_CAT;
        }
        switch (category) {
            case "I":
                return "Informational";
            case "R":
                return "Refactor";
            case "C":
                return "Convention";
            case "W":
                return "Warning";
            case "E":
                return "Error";
            case "F":
                return "Fatal";

            default:
                return UNKNOWN_CAT;
        }
    }

    private Severity mapPriority(final String category) {
        // First letter of the Pylint classification is one of F/E/W/R/C. E/F/W are high
        // priority.

        // See http://docs.pylint.org/output.html for definitions of the categories
        if (StringUtils.isEmpty(category)) {
            // if the category is missing from the output, default to 'normal'.
            return Severity.WARNING_NORMAL;
        }
        switch (category) {
            // [I]nformational messages that Pylint emits (do not contribute to your analysis score)
            // [R]efactor for a ?good practice? metric violation
            // [C]onvention for coding standard violation
            case "I":
            case "R":
            case "C":
                return Severity.WARNING_LOW;

            // [W]arning for stylistic problems, or minor programming issues
            case "W":
                return Severity.WARNING_NORMAL;

            // [E]rror for important programming issues (i.e. most probably bug)
            case "E":
                return Severity.WARNING_HIGH;
            // [F]atal for errors which prevented further processing
            case "F":
                return Severity.ERROR;

            default:
                return Severity.WARNING_LOW;
        }
    }
}
