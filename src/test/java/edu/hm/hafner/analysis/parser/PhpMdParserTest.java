package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link PhpMdParser}.
 *
 * @author Akash Manna
 */
class PhpMdParserTest extends AbstractParserTest {
    PhpMdParserTest() {
        super("phpmd-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("src/Calculator.php")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Design Rules")
                .hasType("CyclomaticComplexity")
                .hasMessage("The class Calculator has an overall complexity is too high")
                .hasLineStart(5)
                .hasLineEnd(25);

        softly.assertThat(report.get(1))
                .hasFileName("src/Calculator.php")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Naming Rules")
                .hasType("ShortVariable")
                .hasMessage("Avoid variables with short names like $x. Configured minimum length is 3.")
                .hasLineStart(8)
                .hasLineEnd(8);

        softly.assertThat(report.get(2))
                .hasFileName("src/Helper.php")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Design Rules")
                .hasType("UnusedLocalVariable")
                .hasMessage("Avoid unused local variables such as '$unused'.")
                .hasLineStart(15)
                .hasLineEnd(15);

        softly.assertThat(report.get(3))
                .hasFileName("src/Helper.php")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Code Size Rules")
                .hasType("TooManyMethods")
                .hasMessage("The class Helper has 15 methods. Consider refactoring.")
                .hasLineStart(1)
                .hasLineEnd(50);
    }

    @Test
    void shouldParseMultipleFiles() {
        var report = parse("phpmd-report-multiple-files.json");

        assertThat(report).hasSize(5).hasDuplicatesSize(0);

        assertThat(report.get(0))
                .hasFileName("src/Model/User.php")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Design Rules")
                .hasType("LongParameterList")
                .hasMessage("The method User::create() has 8 parameters. Consider reducing the number of parameters to less than 5.")
                .hasLineStart(30)
                .hasLineEnd(35);

        assertThat(report.get(1))
                .hasFileName("src/Model/User.php")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Naming Rules")
                .hasType("LongVariable")
                .hasMessage("Avoid excessively long variable names like $veryLongVariableName. Keep variable name length under 20 characters.")
                .hasLineStart(42)
                .hasLineEnd(42);

        assertThat(report.get(2))
                .hasFileName("src/Service/UserService.php")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Design Rules")
                .hasType("ExcessiveMethodLength")
                .hasMessage("The method UserService::processUser() has 150 lines of code. Current threshold is 100. Reduce complexity by breaking into smaller methods.")
                .hasLineStart(10)
                .hasLineEnd(160);

        assertThat(report.get(3))
                .hasFileName("src/Service/UserService.php")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Design Rules")
                .hasType("TooManyPublicMethods")
                .hasMessage("The class UserService has 12 public methods. Consider refactoring to reduce public interface.")
                .hasLineStart(1)
                .hasLineEnd(200);

        assertThat(report.get(4))
                .hasFileName("src/Util/Helper.php")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Code Size Rules")
                .hasType("ExcessiveClassComplexity")
                .hasMessage("The class Helper has 25 methods making it too complex. Consider breaking it into smaller classes.")
                .hasLineStart(5)
                .hasLineEnd(150);
    }

    @Test
    void shouldHandleEmptyReports() {
        var report = parse("phpmd-report-empty.json");

        assertThat(report).hasSize(0).hasDuplicatesSize(0);
    }

    @Override
    protected IssueParser createParser() {
        return new PhpMdParser();
    }
}
