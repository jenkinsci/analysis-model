package edu.hm.hafner.analysis.parser;

import java.nio.file.Paths;
import java.util.Iterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link RfLintParser}.
 *
 * @author traitanit
 * @author Bassam Khouri
 */
class RfLintParserTest extends AbstractParserTest {
    private static final String SUITE_CATEGORY = "Suite";
    private static final String OTHER_CATEGORY = "Other";
    private static final String KEYWORD_CATEGORY = "Keyword";
    private static final String TEST_CASE_CATEGORY = "Test Case";
    private static final String CUSTOM_CATEGORY = "Custom";
    private static final String ISSUES_FILE = "rflint.txt";

    /**
     * Creates a new instance of {@link RfLintParserTest}.
     */
    RfLintParserTest() {
        super(ISSUES_FILE);
    }

    @SuppressWarnings("methodlength")
    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(25);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasLineStart(25)
                .hasColumnStart(100)
                .hasType("LineTooLong")
                .hasMessage("Line is too long (exceeds 100 characters)")
                .hasFileName("./Login_to_web.robot")
                .hasPackageName(".")
                .hasCategory(OTHER_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(40)
                .hasColumnStart(0)
                .hasType("RequireKeywordDocumentation")
                .hasMessage("No keyword documentation")
                .hasFileName("./Login_to_web.robot")
                .hasPackageName(".")
                .hasCategory(KEYWORD_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(24)
                .hasColumnStart(100)
                .hasType("LineTooLong")
                .hasMessage("Line is too long (exceeds 100 characters)")
                .hasFileName("./Merchant_Signup.robot")
                .hasPackageName(".")
                .hasCategory(OTHER_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(378)
                .hasColumnStart(0)
                .hasType("RequireKeywordDocumentation")
                .hasMessage("No keyword documentation")
                .hasFileName("./Merchant_Signup.robot")
                .hasPackageName(".")
                .hasCategory(KEYWORD_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(73)
                .hasLineEnd(73)
                .hasColumnStart(0)
                .hasType("TooFewKeywordSteps")
                .hasMessage("Too few steps (1) in keyword")
                .hasFileName("./merchant_common_keyword.txt")
                .hasPackageName(".")
                .hasCategory(KEYWORD_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(123)
                .hasLineEnd(123)
                .hasColumnStart(34)
                .hasType("Ignore Error")
                .hasMessage("Ignore Error")
                .hasFileName("./merchant_common_keyword.txt")
                .hasPackageName(".")
                .hasCategory(CUSTOM_CATEGORY)
                .hasSeverity(Severity.WARNING_LOW);

        String filename = "./foo/MyLinter.Testing.robot";
        String packageName = Paths.get(".", "foo").toString();

        softly.assertThat(iterator.next())
                .hasLineStart(2)
                .hasColumnStart(0)
                .hasType("RequireSuiteDocumentation")
                .hasMessage("No suite documentation")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(SUITE_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(62)
                .hasColumnStart(0)
                .hasType("TrailingBlankLines")
                .hasMessage("Too many trailing blank lines")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(OTHER_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasType("PeriodInSuiteName")
                .hasMessage("'.' in suite name 'MyLinter.Testing'")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(SUITE_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(10)
                .hasColumnStart(0)
                .hasType("InvalidTable")
                .hasMessage("Unknown table name 'InvalidTableName'")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(SUITE_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(54)
                .hasColumnStart(0)
                .hasType("DuplicateKeywordNames")
                .hasMessage("Duplicate keyword name 'My Keyword 1'")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(SUITE_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(28)
                .hasColumnStart(0)
                .hasType("DuplicateTestNames")
                .hasMessage("Duplicate testcase name 'Foo Test Case'")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(SUITE_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(2)
                .hasColumnStart(0)
                .hasType("TableSpacing")
                .hasMessage("There should not be a blank space after the Settings table name")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(CUSTOM_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(25)
                .hasColumnStart(0)
                .hasType("PeriodInTestName")
                .hasMessage("'.' in testcase name 'This.Test. Has. Been. Free.'")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(TEST_CASE_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(26)
                .hasColumnStart(0)
                .hasType("RequireTestDocumentation")
                .hasMessage("No testcase documentation")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(SUITE_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(28)
                .hasColumnStart(0)
                .hasType("TagWithSpaces")
                .hasMessage("space not allowed in tag name: 'Tag Name with Space'")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(TEST_CASE_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(37)
                .hasColumnStart(0)
                .hasType("RequireKeywordDocumentation")
                .hasMessage("No keyword documentation")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(KEYWORD_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(41)
                .hasColumnStart(0)
                .hasType("CustomRuleName")
                .hasMessage("This is a custom rule violation")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(CUSTOM_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(93)
                .hasColumnStart(0)
                .hasType("TooFewKeywordSteps")
                .hasMessage("Too few steps (1) in keyword")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(KEYWORD_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(70)
                .hasColumnStart(0)
                .hasType("TooFewTestSteps")
                .hasMessage("Too few steps (1) in test case")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(TEST_CASE_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(96)
                .hasColumnStart(0)
                .hasType("TooManyTestCases")
                .hasMessage("Too many test cases (33 > 10) in test suite")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(SUITE_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(180)
                .hasColumnStart(0)
                .hasType("TrailingWhitespace")
                .hasMessage("Line has trailing whitespace")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(OTHER_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(203)
                .hasColumnStart(0)
                .hasType("TooManyTestSteps")
                .hasMessage("Too many steps (11) in test case")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(TEST_CASE_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(301)
                .hasColumnStart(0)
                .hasType("FileTooLong")
                .hasMessage("File has too many lines (676)")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(OTHER_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(676)
                .hasColumnStart(0)
                .hasType("TrailingBlankLines")
                .hasMessage("Too many trailing blank lines")
                .hasFileName(filename)
                .hasPackageName(packageName)
                .hasCategory(OTHER_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected RfLintParser createParser() {
        return new RfLintParser();
    }
}
