package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link SqlFluffParser}.
 *
 * @author Akash Manna
 */
class SqlFluffParserTest extends AbstractParserTest {
    SqlFluffParserTest() {
        super("sqlfluff.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("queries/users.sql")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(1)
                .hasColumnEnd(10)
                .hasType("L001")
                .hasMessage("Expected indentation of 4, got 8")
                .hasCategory("Indentation")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("queries/orders.sql")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(15)
                .hasColumnEnd(20)
                .hasType("L002")
                .hasMessage("Missing space before operator")
                .hasCategory("Whitespace")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("queries/products.sql")
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasColumnStart(5)
                .hasColumnEnd(20)
                .hasType("L003")
                .hasMessage("Expected camelCase names to be in snake_case")
                .hasCategory("Names")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(3))
                .hasFileName("queries/inventory.sql")
                .hasLineStart(15)
                .hasLineEnd(15)
                .hasColumnStart(1)
                .hasColumnEnd(6)
                .hasType("L004")
                .hasMessage("Expected 'SELECT' to be capitalized")
                .hasCategory("Keywords")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new SqlFluffParser();
    }

    @Test
    void accepts() {
        assertThat(new SqlFluffParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("sqlfluff.json")))).isTrue();
        assertThat(new SqlFluffParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("sqlfluff-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleEdgeCases() {
        var report = parse("sqlfluff-edge-cases.json");

        assertThat(report).hasSize(3);

        assertThat(report.get(0))
                .hasFileName("test.sql")
                .hasType("L005")
                .hasMessage("Inconsistent style detected")
                .hasCategory("Style")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(1))
                .hasFileName("edge_case.sql")
                .hasLineStart(20)
                .hasLineEnd(20)
                .hasType("L006")
                .hasMessage("Missing rule name and description");

        assertThat(report.get(2))
                .hasFileName("format.sql")
                .hasLineStart(25)
                .hasColumnStart(10)
                .hasType("L007")
                .hasMessage("Format")
                .hasCategory("Format");
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("sqlfluff");

        assertThat(descriptor.getPattern()).isEqualTo("**/sqlfluff.json");
        assertThat(descriptor.getHelp()).contains("sqlfluff lint --format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://www.sqlfluff.com/");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Test
    void shouldVerifyDescriptorType() {
        var descriptor = new ParserRegistry().get("sqlfluff");

        assertThat(descriptor.getType()).isEqualTo(Report.IssueType.WARNING);
    }

    @Test
    void shouldHandleMultiLineViolations() {
        var report = parse("sqlfluff.json");

        assertThat(report).hasSize(4);
        for (var issue : report) {
            if (issue.getLineStart() != 0) {
                assertThat(issue.getLineEnd()).isGreaterThanOrEqualTo(issue.getLineStart());
            }
        }
    }

    @Test
    void shouldHandleColumnInformation() {
        var report = parse("sqlfluff.json");

        assertThat(report).hasSize(4);

        var firstIssue = report.get(0);
        assertThat(firstIssue.getColumnStart()).isEqualTo(1);
        assertThat(firstIssue.getColumnEnd()).isEqualTo(10);

        var secondIssue = report.get(1);
        assertThat(secondIssue.getColumnStart()).isEqualTo(15);
        assertThat(secondIssue.getColumnEnd()).isEqualTo(20);
    }

    @Test
    void shouldHandleRuleCodeAndName() {
        var report = parse("sqlfluff.json");

        assertThat(report.get(0).getType()).isEqualTo("L001");
        assertThat(report.get(1).getType()).isEqualTo("L002");
        assertThat(report.get(2).getType()).isEqualTo("L003");
        assertThat(report.get(3).getType()).isEqualTo("L004");

        assertThat(report.get(0).getCategory()).isEqualTo("Indentation");
        assertThat(report.get(1).getCategory()).isEqualTo("Whitespace");
        assertThat(report.get(2).getCategory()).isEqualTo("Names");
        assertThat(report.get(3).getCategory()).isEqualTo("Keywords");
    }

    @Test
    void shouldBuildCorrectMessagePriority() {
        var report = parse("sqlfluff-edge-cases.json");

        assertThat(report.get(0).getMessage())
                .isEqualTo("Inconsistent style detected");  

        assertThat(report.get(1).getMessage())
                .isEqualTo("Missing rule name and description");  

        assertThat(report.get(2).getMessage())
                .isEqualTo("Format");  
    }

    @Test
    void shouldHandlePathAsFileName() {
        var report = parse("sqlfluff.json");

        assertThat(report.get(0).getFileName()).isEqualTo("queries/users.sql");
        assertThat(report.get(1).getFileName()).isEqualTo("queries/orders.sql");
        assertThat(report.get(2).getFileName()).isEqualTo("queries/products.sql");
        assertThat(report.get(3).getFileName()).isEqualTo("queries/inventory.sql");
    }

    @Test
    void shouldHandleComprehensiveMessageCoverage() {
        var report = parse("sqlfluff-comprehensive.json");

        assertThat(report).hasSize(9);

        assertThat(report.get(0))
                .hasType("L008")
                .hasMessage("Only description field available")
                .hasFileName("test1.sql")
                .hasLineStart(1);

        assertThat(report.get(1))
                .hasType("L009")
                .hasMessage("Test description")
                .hasFileName("test2.sql");

        assertThat(report.get(2))
                .hasType("L010")
                .hasMessage("")
                .hasFileName("test3.sql");

        assertThat(report.get(3))
                .hasType("L011")
                .hasMessage("OnlyRuleName")
                .hasFileName("test4.sql")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(4))
                .hasType("L012")
                .hasMessage("")
                .hasFileName("test5.sql");

        assertThat(report.get(5))
                .hasType("L013")
                .hasMessage("RuleName")
                .hasFileName("test6.sql")
                .hasLineStart(5)
                .hasLineEnd(10)
                .hasColumnStart(3)
                .hasColumnEnd(8)
                .hasCategory("RuleName");

        assertThat(report.get(6))
                .hasType("L014")
                .hasFileName("test7.sql")
                .hasColumnStart(2)
                .hasColumnEnd(7)
                .hasCategory("ColumnOnly");

        assertThat(report.get(7))
                .hasType("L015")
                .hasFileName("test8.sql")
                .hasLineStart(15)
                .hasColumnStart(5)
                .hasCategory("MixedFields");

        assertThat(report.get(8))
                .hasType("L016")
                .hasFileName("test9.sql")
                .hasLineEnd(20)
                .hasColumnEnd(10)
                .hasCategory("EndLinesOnly");
    }

    @Test
    void shouldHandleAllFieldCombinations() {
        var report = parse("sqlfluff-comprehensive.json");

        assertThat(report).hasSize(9);

        for (var issue : report) {
            assertThat(issue.getSeverity()).isEqualTo(Severity.WARNING_NORMAL);
        }

        assertThat(report.get(0).getCategory()).isEmpty(); 
        assertThat(report.get(1).getCategory()).isEmpty(); 
        assertThat(report.get(2).getCategory()).isEmpty(); 
        assertThat(report.get(3).getCategory()).isEqualTo("OnlyRuleName");
        assertThat(report.get(4).getCategory()).isEmpty(); 
        assertThat(report.get(5).getCategory()).isEqualTo("RuleName");
        assertThat(report.get(6).getCategory()).isEqualTo("ColumnOnly");
        assertThat(report.get(7).getCategory()).isEqualTo("MixedFields");
        assertThat(report.get(8).getCategory()).isEqualTo("EndLinesOnly");
    }

    @Test
    void shouldHandleMissingOptionalFields() {
        var report = parse("sqlfluff-comprehensive.json");

        var issue0 = report.get(0);
        assertThat(issue0.getLineStart()).isEqualTo(1);
        assertThat(issue0.getLineEnd()).isEqualTo(1);

        var issue1 = report.get(1);
        assertThat(issue1.getLineStart()).isEqualTo(0);
        assertThat(issue1.getLineEnd()).isEqualTo(0);
        assertThat(issue1.getColumnStart()).isEqualTo(0);
        assertThat(issue1.getColumnEnd()).isEqualTo(0);

        var issue5 = report.get(5);
        assertThat(issue5.getLineStart()).isEqualTo(5);
        assertThat(issue5.getLineEnd()).isEqualTo(10);
        assertThat(issue5.getColumnStart()).isEqualTo(3);
        assertThat(issue5.getColumnEnd()).isEqualTo(8);

        var issue6 = report.get(6);
        assertThat(issue6.getLineStart()).isEqualTo(0);
        assertThat(issue6.getColumnStart()).isEqualTo(2);
        assertThat(issue6.getColumnEnd()).isEqualTo(7);

        var issue7 = report.get(7);
        assertThat(issue7.getLineStart()).isEqualTo(15);
        assertThat(issue7.getColumnStart()).isEqualTo(5);

        var issue8 = report.get(8);
        assertThat(issue8.getLineEnd()).isEqualTo(20);
        assertThat(issue8.getColumnEnd()).isEqualTo(10);
    }

    @Test
    void shouldHandleJsonObjectWithoutViolationsKey() {
        var report = parse("sqlfluff-no-violations-key.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleDirectJsonArrayFormat() {
        var report = parse("sqlfluff-array-format.json");

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasType("L001")
                .hasMessage("Direct array violation")
                .hasFileName("direct.sql")
                .hasLineStart(1)
                .hasCategory("DirectArray")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(1))
                .hasType("L002")
                .hasMessage("AnotherOne")
                .hasFileName("another.sql")
                .hasLineStart(5)
                .hasCategory("AnotherOne")
                .hasSeverity(Severity.WARNING_NORMAL);
    }
}
