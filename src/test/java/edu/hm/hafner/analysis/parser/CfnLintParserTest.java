package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import java.nio.file.FileSystems;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link CfnLintParser}.
 *
 * @author Akash Manna
 */
class CfnLintParserTest extends AbstractParserTest {
    CfnLintParserTest() {
        super("cfn-lint-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("templates/vpc.yaml")
                .hasLineStart(2)
                .hasColumnStart(1)
                .hasLineEnd(2)
                .hasColumnEnd(18)
                .hasType("E1001")
                .hasMessage("Template format error: Resources section is required")
                .hasDescription("Validates basic template structure")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(1))
                .hasFileName("templates/network.yaml")
                .hasLineStart(21)
                .hasColumnStart(9)
                .hasLineEnd(21)
                .hasColumnEnd(23)
                .hasType("W1031")
                .hasMessage("Hardcoded bucket names can cause deployment collisions")
                .hasDescription("Checks for values that should be parameterized")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("templates/app.yaml")
                .hasLineStart(35)
                .hasColumnStart(5)
                .hasLineEnd(35)
                .hasColumnEnd(16)
                .hasType("I3011")
                .hasMessage("Function timeout can be tuned to improve cost efficiency")
                .hasDescription("Provides optimization hints")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Override
    protected IssueParser createParser() {
        return new CfnLintParser();
    }

    @Test
    void accepts() {
        var parser = new CfnLintParser();
        assertThat(parser.accepts(read("cfn-lint-report.json"))).isTrue();
        assertThat(parser.accepts(read("foo.txt"))).isFalse();
    }

    private FileReaderFactory read(final String first) {
        return new FileReaderFactory(FileSystems.getDefault().getPath(first));
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("issues-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingRuleAndLocation() {
        var report = parseStringContent("""
                [
                  {
                    "Message": "fallback handling"
                  }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("fallback handling")
                .hasFileName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldHandleWrappedMatchesObject() {
        var report = parseStringContent("""
                {
                  "matches": [
                    {
                      "Filename": "template.yaml",
                      "Level": "Warning",
                      "Message": "wrapped payload",
                      "Rule": {
                        "Id": "W1000"
                      },
                      "Location": {
                        "Start": {
                          "LineNumber": 3,
                          "ColumnNumber": 7
                        },
                        "End": {
                          "LineNumber": 3,
                          "ColumnNumber": 14
                        }
                      }
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("template.yaml")
                .hasType("W1000")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("wrapped payload")
                .hasLineStart(3)
                .hasColumnStart(7)
                .hasLineEnd(3)
                .hasColumnEnd(14);
    }

    @Test
    void shouldSkipStartAndEndIfMissing() {
        var report = parseStringContent("""
                {
                  "matches": [
                    {
                      "Filename": "template.yaml",
                      "Level": "Warning",
                      "Message": "wrapped payload",
                      "Rule": {
                        "Id": "W1000"
                      },
                      "Location": {
                      }
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("template.yaml")
                .hasType("W1000")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("wrapped payload")
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasLineEnd(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("cfn-lint");

        assertThat(descriptor.getPattern()).isEqualTo("**/cfn-lint-report.json");
        assertThat(descriptor.getHelp()).contains("cfn-lint --format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/aws-cloudformation/cfn-lint");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}
