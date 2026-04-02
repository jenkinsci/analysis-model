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
 * Tests the class {@link CheckovParser}.
 *
 * @author Akash Manna
 */
class CheckovParserTest extends AbstractParserTest {
    CheckovParserTest() {
        super("checkov-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("terraform/main.tf")
                .hasLineStart(12)
                .hasLineEnd(20)
                .hasType("CKV_AWS_20")
                .hasMessage("S3 Bucket has an ACL defined which allows public READ access.")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("aws_s3_bucket.public_data")
                .hasDescription("https://docs.bridgecrew.io/docs/s3_1-acl-read-permissions-everyone");

        softly.assertThat(report.get(1))
                .hasFileName("terraform/network.tf")
                .hasLineStart(30)
                .hasLineEnd(30)
                .hasType("CKV_AWS_130")
                .hasMessage("Ensure VPC subnets do not assign public IP by default")
                .hasSeverity(Severity.ERROR)
                .hasCategory("aws_subnet.public")
                .hasDescription("https://docs.bridgecrew.io/docs/bc_aws_networking_31");

        softly.assertThat(report.get(2))
                .hasFileName("kubernetes/deployment.yaml")
                .hasLineStart(8)
                .hasLineEnd(12)
                .hasType("CKV_K8S_13")
                .hasMessage("Memory limits should be set")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Deployment.default.web");
    }

    @Override
    protected IssueParser createParser() {
        return new CheckovParser();
    }

    @Test
    void accepts() {
        var parser = new CheckovParser();

        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("checkov-report.json"))))
                .isTrue();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("checkov-report.txt"))))
                .isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() {
        var report = parseStringContent("{}");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleTopLevelFailedChecks() {
        var report = parseStringContent("""
                {
                    "failed_checks": [
                        {
                            "check_id": "CKV_CUSTOM_1",
                            "check_name": "Custom check",
                            "severity": "MEDIUM",
                            "file_path": "custom/file.tf",
                            "file_line_range": [3]
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("CKV_CUSTOM_1")
                .hasMessage("Custom check")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasFileName("custom/file.tf")
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasCategory(DEFAULT_CATEGORY)
                .hasDescription("");
    }

    @Test
    void shouldSkipNonObjectFailedCheckEntries() {
        var report = parseStringContent("""
                {
                    "failed_checks": [
                        42,
                        "invalid",
                        {
                            "check_id": "CKV_CUSTOM_2",
                            "check_name": "Valid object",
                            "severity": "LOW",
                            "file_path": "valid/file.tf",
                            "file_line_range": [7, 9]
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("CKV_CUSTOM_2")
                .hasMessage("Valid object")
                .hasSeverity(Severity.WARNING_LOW)
                .hasFileName("valid/file.tf")
                .hasLineStart(7)
                .hasLineEnd(9);
    }

    @Test
    void shouldHandleMissingFields() {
        var report = parseStringContent("""
                {
                    "results": {
                        "failed_checks": [
                            {
                                "severity": "LOW"
                            }
                        ]
                    }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasMessage("-")
                .hasSeverity(Severity.WARNING_LOW)
                .hasFileName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasCategory(DEFAULT_CATEGORY)
                .hasDescription("");
    }

    @Test
    void shouldHandleResultsWithoutFailedChecks() {
        var report = parseStringContent("""
                {
                    "results": {
                        "passed_checks": []
                    }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleEmptyLineRangeArray() {
        var report = parseStringContent("""
                {
                    "failed_checks": [
                        {
                            "check_id": "CKV_CUSTOM_3",
                            "check_name": "Empty line range",
                            "severity": "MEDIUM",
                            "file_path": "empty-range.tf",
                            "file_line_range": []
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("CKV_CUSTOM_3")
                .hasLineStart(0)
                .hasLineEnd(0);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("checkov");

        assertThat(descriptor.getPattern()).isEqualTo("**/checkov-report.json");
        assertThat(descriptor.getHelp()).contains("checkov --output json --output-file-path checkov-report.json");
        assertThat(descriptor.getUrl()).isEqualTo("https://www.checkov.io/");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}