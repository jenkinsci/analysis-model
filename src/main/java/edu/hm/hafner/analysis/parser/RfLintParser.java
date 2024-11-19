package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import static edu.hm.hafner.analysis.Categories.*;

/**
 * A parser for <a href="http://robotframework.org/">Robot Framework</a>. Parses output from <a
 * href="https://github.com/boakley/robotframework-lint">robotframework-lint</a>.
 * To generate rflint file use:
 * {@code
 * <pre>
 *     cmd$ pip install robotframework-lint
 *     cmd$ rflint path/to/test.robot
 * </pre>}
 *
 * @author traitanit
 * @author Bassam Khouri
 */
public class RfLintParser extends IssueParser {
    /**
     * Map of Robot Framework severity to the analysis model severity.
     */
    private enum RfLintSeverity {
        ERROR(Severity.WARNING_HIGH),
        E(ERROR),
        WARNING(Severity.WARNING_NORMAL),
        W(WARNING),
        IGNORE(Severity.WARNING_LOW),
        I(IGNORE);

        private final Severity severityLevel;

        RfLintSeverity(final Severity level) {
            severityLevel = level;
        }

        RfLintSeverity(final RfLintSeverity e) {
            this(e.severityLevel);
        }

        public Severity getSeverityLevel() {
            return severityLevel;
        }

        /**
         * Determines the RfLintSeverity based on the provided character.
         *
         * @param violationSeverity
         *         The character presentiting the violation severity
         *
         * @return An instance of RfLintSeverity matching the character. `WARNING` as the default if the severity
         *         character is not valid.
         */
        public static RfLintSeverity fromCharacter(final char violationSeverity) {
            if (EnumUtils.isValidEnum(RfLintSeverity.class, String.valueOf(violationSeverity))) {
                return RfLintSeverity.valueOf(String.valueOf(violationSeverity));
            }
            return WARNING;
        }
    }

    /**
     * The possible categories.
     */
    private enum RfLintCategory {
        SUITE("Suite"),
        KEYWORD("Keyword"),
        TEST_CASE("Test Case"),
        OTHER("Other"),
        CUSTOM("Custom");

        private final String name;

        RfLintCategory(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * The list of the rule names built into rflint.
     */
    private enum RfLintRuleName {
        DUPLICATE_KEYWORD_NAMES(RfLintCategory.SUITE),
        DUPLICATE_TEST_NAMES(RfLintCategory.SUITE),
        FILE_TOO_LONG(RfLintCategory.OTHER),
        INVALID_TABLE(RfLintCategory.SUITE),
        LINE_TOO_LONG(RfLintCategory.OTHER),
        PERIOD_IN_SUITE_NAME(RfLintCategory.SUITE),
        PERIOD_IN_TEST_NAME(RfLintCategory.TEST_CASE),
        REQUIRE_KEYWORD_DOCUMENTATION(RfLintCategory.KEYWORD),
        REQUIRE_SUITE_DOCUMENTATION(RfLintCategory.SUITE),
        REQUIRE_TEST_DOCUMENTATION(RfLintCategory.SUITE),
        TAG_WITH_SPACES(RfLintCategory.TEST_CASE),
        TOO_FEW_KEYWORD_STEPS(RfLintCategory.KEYWORD),
        TOO_MANY_TEST_CASES(RfLintCategory.SUITE),
        TOO_FEW_TEST_STEPS(RfLintCategory.TEST_CASE),
        TOO_MANY_TEST_STEPS(RfLintCategory.TEST_CASE),
        TRAILING_BLANK_LINES(RfLintCategory.OTHER),
        TRAILING_WHITESPACE(RfLintCategory.OTHER),
        UNKNOWN(RfLintCategory.CUSTOM);

        private static final boolean CAPITALIZE_FIRST_LETTER = true;
        private final RfLintCategory category;

        RfLintRuleName(final RfLintCategory category) {
            this.category = category;
        }

        public RfLintCategory getCategory() {
            return category;
        }

        /**
         * Determines the RfLintRuleName based on the provided name.
         *
         * @param name
         *         the name of the rule
         *
         * @return An instance of RfLintRuleName matching the name. `UNKNOWN` as the default if the name is not valid.
         */
        public static RfLintRuleName fromName(final String name) {
            for (RfLintRuleName rule : values()) {
                if (CaseUtils.toCamelCase(rule.name(), CAPITALIZE_FIRST_LETTER, '_').equals(name)) {
                    return rule;
                }
            }
            return UNKNOWN;
        }
    }

    @Serial
    private static final long serialVersionUID = -7903991158616386226L;

    private String fileName = StringUtils.EMPTY;
    private static final Pattern WARNING_PATTERN = Pattern.compile(
            "(?<severity>[WEI]): (?<lineNumber>\\d+), (?<columnNumber>\\d+): (?<message>.*) \\((?<ruleName>.*)\\)");
    private static final Pattern FILE_PATTERN = Pattern.compile("\\+\\s(?<filename>.*)");

    @Override
    public Report parse(final ReaderFactory readerFactory) {
        try (Stream<String> lines = readerFactory.readStream(); var builder = new IssueBuilder()) {
            var warnings = new Report();
            lines.forEach(line -> parseLine(builder, warnings, line));
            return warnings;
        }
    }

    @SuppressWarnings("PMD.DoNotUseThreads")
    private void parseLine(final IssueBuilder builder, final Report warnings, final String line) {
        var fileMatcher = FILE_PATTERN.matcher(line);
        if (fileMatcher.find()) {
            fileName = fileMatcher.group(1);
        }
        var matcher = WARNING_PATTERN.matcher(line);
        if (matcher.find()) {
            warnings.add(createIssue(matcher, builder));
        }
        if (Thread.interrupted()) {
            throw new ParsingCanceledException();
        }
    }

    private Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        var message = matcher.group("message");
        var severityStr = guessCategoryIfEmpty(matcher.group("severity"), message);
        var priority = RfLintSeverity.fromCharacter(severityStr.charAt(0)).getSeverityLevel();
        var ruleName = matcher.group("ruleName");

        var category = RfLintRuleName.fromName(ruleName).getCategory();

        return builder.setFileName(fileName)
                .setPackageName(Objects.toString(Path.of(fileName).getParent()))
                .setLineStart(matcher.group("lineNumber"))
                .setColumnStart(matcher.group("columnNumber"))
                .setCategory(category.getName())
                .setType(ruleName)
                .setMessage(message)
                .setSeverity(priority)
                .buildAndClean();
    }
}
