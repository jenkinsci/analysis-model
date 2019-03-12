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

import static edu.hm.hafner.analysis.Categories.guessCategoryIfEmpty;

/**
 * A parser for <a href="http://robotframework.org/">Robot Framework</a> Parse output from <a
 * href="https://github.com/boakley/robotframework-lint">robotframework-lint</a>. To generate rflint file cmd$ pip
 * install robotframework-lint cmd$ rflint path/to/test.robot
 *
 * @author traitanit
 */
public class RfLintParser extends IarParser {

    /**
     *  Map of Robot Framework severity to the analysis model severity.
     */
    private enum RfLintSeverity {
        ERROR(Severity.WARNING_HIGH),
        E(ERROR),
        WARNING(Severity.WARNING_NORMAL),
        W(WARNING),
        IGNORE(Severity.WARNING_LOW),
        I(IGNORE);

        private final Severity severityLevel;

        RfLintSeverity(Severity level) {
            this.severityLevel = level;
        }

        RfLintSeverity(RfLintSeverity e) {
            this(e.severityLevel);
        }

        public Severity getSeverityLevel() {
            return this.severityLevel;
        }

        /**
         * Determines the RfLintSeverity based on the provided character.
         *
         * @param violationSeverity The character presentiting the violation severity
         * @return An instance of RfLintSeverity matching the character.  `WARNING` as the default if the severity
         *      character is not valid.
         */
        public static RfLintSeverity fromCharacter(final char violationSeverity) {
            RfLintSeverity severity = WARNING;
            if (EnumUtils.isValidEnum(RfLintSeverity.class, String.valueOf(violationSeverity))) {
                severity = RfLintSeverity.valueOf(String.valueOf(violationSeverity));
            }
            return severity;
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

        RfLintCategory(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        /**
         * Determines the RfLint category based on the provided rule name.
         *
         * @param ruleName The rule name
         * @return An instance of the RfLintCategory associated with the rule name.
         */
        public static RfLintCategory fromRuleName(final String ruleName) {
            RfLintCategory category = RfLintCategory.CUSTOM;
            if (EnumUtils.isValidEnum(RfLintRuleName.class, ruleName)) {
                RfLintRuleName rule = RfLintRuleName.valueOf(ruleName);
                category = rule.getCategory();
            }
            return category;
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

        RfLintRuleName(RfLintCategory category) {
            this.category = category;
        }

        public RfLintCategory getCategory() {
            return this.category;
        }

    }

    private static final long serialVersionUID = -7903991158616386226L;

    private String fileName = StringUtils.EMPTY;
    private static final Pattern WARNING_PATTERN = Pattern.compile("(?<severity>[W|E|I]): (?<lineNumber>\\d+), (?<columnNumber>\\d+): (?<message>.*) \\((?<ruleName>.*)\\)");
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
                    Optional<Issue> warning = createIssue(matcher, new IssueBuilder());
                    if (warning.isPresent()) {
                        warnings.add(warning.get());
                    }

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
