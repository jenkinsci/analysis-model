package edu.hm.hafner.analysis.parser;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import static edu.hm.hafner.analysis.Categories.*;

/**
 * A parser for <a href="http://robotframework.org/">Robot Framework</a>. Parses output from <a
 * href="https://github.com/boakley/robotframework-lint">robotframework-lint</a>.
 * To generate rflint file use: <pre>
 *     cmd$ pip install robotframework-lint
 *     cmd$ rflint path/to/test.robot
 * </pre>
 *
 * @author traitanit
 */
public class RfLintParser extends IarParser {
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
            this.severityLevel = level;
        }

        RfLintSeverity(final RfLintSeverity e) {
            this(e.severityLevel);
        }

        public Severity getSeverityLevel() {
            return this.severityLevel;
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

        private String name;

        RfLintCategory(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        /**
         * Determines the RfLint category based on the provided rule name.
         *
         * @param ruleName
         *         The rule name
         *
         * @return An instance of the RfLintCategory associated with the rule name.
         */
        public static RfLintCategory fromRuleName(final String ruleName) {
            if (EnumUtils.isValidEnum(RfLintRuleName.class, ruleName)) {
                RfLintRuleName rule = RfLintRuleName.valueOf(ruleName);
                return rule.getCategory();
            }
            return RfLintCategory.CUSTOM;
        }
    }

    /**
     * The list of the rule names built into rflint.
     */
    private enum RfLintRuleName {
        DuplicateKeywordNames(RfLintCategory.SUITE),
        DuplicateTestNames(RfLintCategory.SUITE),
        FileTooLong(RfLintCategory.OTHER),
        InvalidTable(RfLintCategory.SUITE),
        LineTooLong(RfLintCategory.OTHER),
        PeriodInSuiteName(RfLintCategory.SUITE),
        PeriodInTestName(RfLintCategory.TEST_CASE),
        RequireKeywordDocumentation(RfLintCategory.KEYWORD),
        RequireSuiteDocumentation(RfLintCategory.SUITE),
        RequireTestDocumentation(RfLintCategory.SUITE),
        TagWithSpaces(RfLintCategory.TEST_CASE),
        TooFewKeywordSteps(RfLintCategory.KEYWORD),
        TooManyTestCases(RfLintCategory.SUITE),
        TooFewTestSteps(RfLintCategory.TEST_CASE),
        TooManyTestSteps(RfLintCategory.TEST_CASE),
        TrailingBlankLines(RfLintCategory.OTHER),
        TrailingWhitespace(RfLintCategory.OTHER);

        private RfLintCategory category;

        RfLintRuleName(final RfLintCategory category) {
            this.category = category;
        }

        public RfLintCategory getCategory() {
            return this.category;
        }

    }

    private static final long serialVersionUID = -7903991158616386226L;

    private String fileName = StringUtils.EMPTY;
    private static final Pattern WARNING_PATTERN = Pattern.compile(
            "(?<severity>[WEI]): (?<lineNumber>\\d+), (?<columnNumber>\\d+): (?<message>.*) \\((?<ruleName>.*)\\)");
    private static final Pattern FILE_PATTERN = Pattern.compile("\\+\\s(?<filename>.*)");

    @Override
    public Report parse(final ReaderFactory readerFactory) {
        try (Stream<String> lines = readerFactory.readStream()) {
            Report warnings = new Report();
            lines.forEach(line -> {
                Matcher fileMatcher = FILE_PATTERN.matcher(line);
                if (fileMatcher.find()) {
                    fileName = fileMatcher.group(1);
                }
                Matcher matcher = WARNING_PATTERN.matcher(line);
                if (matcher.find()) {
                    createIssue(matcher, new IssueBuilder()).ifPresent(warnings::add);
                }
                if (Thread.interrupted()) {
                    throw new ParsingCanceledException();
                }
            });
            return warnings;
        }
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String packageName = Paths.get(fileName).getParent().toString();
        String message = matcher.group("message");
        String severityStr = guessCategoryIfEmpty(matcher.group("severity"), message);
        String ruleName = matcher.group("ruleName");

        char severityCharacter = severityStr.charAt(0);
        RfLintCategory category = RfLintCategory.fromRuleName(ruleName);
        Severity priority = RfLintSeverity.fromCharacter(severityCharacter).getSeverityLevel();

        return builder.setFileName(fileName)
                .setPackageName(packageName)
                .setDirectory(packageName)
                .setLineStart(matcher.group("lineNumber"))
                .setColumnStart(matcher.group("columnNumber"))
                .setCategory(category.getName())
                .setType(ruleName)
                .setMessage(message)
                .setSeverity(priority)
                .buildOptional();
    }
}
