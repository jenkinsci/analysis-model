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
 * Tests the class {@link TerraformLintParser}.
 *
 * @author Akash Manna
 */
class TerraformLintParserTest extends AbstractParserTest {
    TerraformLintParserTest() {
        super("tflint.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("main.tf")
                .hasLineStart(6)
                .hasColumnStart(5)
                .hasColumnEnd(25)
                .hasType("terraform_deprecated_index")
                .hasMessage("Terraform 0.10.x and earlier use index() function")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("modules/aws/main.tf")
                .hasLineStart(10)
                .hasColumnStart(10)
                .hasColumnEnd(35)
                .hasType("aws_instance_invalid_ami")
                .hasMessage("Search base image 'ami-invalid' does not exist")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(2))
                .hasFileName("variables.tf")
                .hasLineStart(3)
                .hasColumnStart(1)
                .hasColumnEnd(20)
                .hasType("terraform_naming_convention")
                .hasMessage("Resource name should match pattern '^[a-z_]*$'")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(3))
                .hasFileName("security.tf")
                .hasLineStart(15)
                .hasColumnStart(12)
                .hasColumnEnd(20)
                .hasType("aws_security_group_ingress_invalid_port")
                .hasMessage("Port value must be between 0 and 65535")
                .hasSeverity(Severity.ERROR);
    }

    @Override
    protected IssueParser createParser() {
        return new TerraformLintParser();
    }

    @Test
    void accepts() {
        assertThat(new TerraformLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("tflint.json")))).isTrue();
        assertThat(new TerraformLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
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
    void shouldHandleMissingRuleAndRange() {
        var report = parseStringContent("""
                {
                    "issues": [
                        {
                            "message": "fallback branches"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("fallback branches")
                .hasFileName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldHandleZeroLineNumbersInRange() {
        var report = parseStringContent("""
                {
                    "issues": [
                        {
                            "rule": {
                                "name": "terraform_custom_rule",
                                "severity": "warning"
                            },
                            "message": "line number defaults",
                            "range": {
                                "filename": "main.tf",
                                "start": {
                                    "line": 0,
                                    "column": 7
                                },
                                "end": {
                                    "line": 0,
                                    "column": 9
                                }
                            }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("terraform_custom_rule")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasFileName("main.tf")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(7)
                .hasColumnEnd(9);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("tflint");

        assertThat(descriptor.getPattern()).isEqualTo("**/tflint-report.json");
        assertThat(descriptor.getHelp()).contains("tflint --format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/terraform-linters/tflint");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}
