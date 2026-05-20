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

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link KicsParser}.
 *
 * @author Akash Manna
 */
class KicsParserTest extends AbstractParserTest {
    KicsParserTest() {
        super("kics-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("assets/queries/ansible/aws/alb_listening_on_http/test/positive.yaml")
                .hasLineStart(11)
                .hasLineEnd(11)
                .hasCategory("Ansible")
                .hasType("f81d63d2-c5d7-43a4-a5b5-66717a41c895")
                .hasMessage("ALB Listening on HTTP")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(0).getDescription())
                .contains("Description: AWS Application Load Balancer (alb) should not listen on HTTP")
                .contains("Query URL: https://docs.ansible.com/ansible/latest/collections/community/aws/elb_application_lb_module.html")
                .contains("Cloud provider: AWS")
                .contains("Category: Networking and Firewall")
                .contains("CWE: 22")
                .contains("Risk score: 6.8")
                .contains("Resource type: community.aws.elb_application_lb")
                .contains("Resource name: my_elb_application")
                .contains("Issue type: IncorrectValue")
                .contains("Search key: name={{my_elb_application}}.{{community.aws.elb_application_lb}}.listeners.Protocol=HTTP")
                .contains("Expected value: 'aws_elb_application_lb' Protocol should be 'HTTP'")
                .contains("Actual value: 'aws_elb_application_lb' Protocol it's not 'HTTP'");

        softly.assertThat(report.get(1))
                .hasFileName("assets/queries/ansible/aws/alb_listening_on_http/test/positive.yaml")
                .hasLineStart(29)
                .hasLineEnd(29)
                .hasCategory("Ansible")
                .hasType("f81d63d2-c5d7-43a4-a5b5-66717a41c895")
                .hasMessage("ALB Listening on HTTP")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(1).getDescription())
                .contains("Issue type: MissingAttribute")
                .contains("Resource name: my_elb_application2")
                .contains("Search key: name={{my_elb_application2}}.{{community.aws.elb_application_lb}}.listeners")
                .contains("Actual value: 'aws_elb_application_lb' Protocol is missing");

        softly.assertThat(report.get(2))
                .hasFileName("terraform/main.tf")
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasCategory("Terraform")
                .hasType("kics-public-bucket")
                .hasMessage("Public S3 bucket")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2).getDescription())
                .contains("Description: S3 buckets should not be publicly accessible")
                .contains("Cloud provider: AWS")
                .contains("Category: Insecure Defaults")
                .contains("CWE: 200")
                .contains("Risk score: 4.0")
                .contains("Resource type: aws_s3_bucket")
                .contains("Resource name: logs_bucket")
                .contains("Issue type: PublicAcl")
                .contains("Expected value: private")
                .contains("Actual value: public-read");
    }

    @Override
    protected IssueParser createParser() {
        return new KicsParser();
    }

    @Test
    void accepts() {
        assertThat(new KicsParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("kics-report.json")))).isTrue();
        assertThat(new KicsParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void shouldHandleMissingQueries() {
        assertThat(parseStringContent("{}\n")).isEmpty();
    }

    @Test
    void shouldSkipNonObjectEntries() {
        var report = parseStringContent("""
                {
                  "queries": [
                    42,
                    "invalid",
                    {
                      "query_name": "Ignored query",
                      "files": [
                        null,
                        7
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingOptionalValues() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Fallback query",
                      "files": [
                        {
                          "line": 0,
                          "resource_name": "",
                          "file_name": ""
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasType("-")
                .hasMessage("Fallback query")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(0)
                .hasLineEnd(0);
    }

    @Test
    void shouldHandleEmptyFilesArray() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Query with empty files",
                      "files": []
                    }
                  ]
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleNullFilesArray() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Query with null files"
                    }
                  ]
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldIncludeSearchLineWhenProvided() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Query with search line",
                      "query_id": "test-001",
                      "files": [
                        {
                          "file_name": "test.yaml",
                          "line": 10,
                          "search_line": 15
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .contains("Search line: 15");
    }

    @Test
    void shouldOmitSearchLineWhenNegative() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Query without search line",
                      "query_id": "test-001",
                      "files": [
                        {
                          "file_name": "test.yaml",
                          "line": 10,
                          "search_line": -1
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .doesNotContain("Search line");
    }

    @Test
    void shouldUseLegacyFieldNames() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "queryName": "Legacy query",
                      "queryId": "legacy-001",
                      "queryUrl": "https://example.com/query",
                      "Platform": "Terraform",
                      "Category": "Security",
                      "CWE": "79",
                      "riskScore": "7.5",
                      "cloudProvider": "AWS",
                      "description": "This is a test",
                      "files": [
                        {
                          "fileName": "legacy.tf",
                          "line": 5,
                          "resourceType": "aws_resource",
                          "resourceName": "my_resource",
                          "issueType": "MissingAttribute",
                          "searchKey": "prop",
                          "searchValue": "value",
                          "expectedValue": "secure",
                          "actualValue": "insecure",
                          "searchLine": 7
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        var issue = report.get(0);
        assertThat(issue)
                .hasFileName("legacy.tf")
                .hasType("legacy-001")
                .hasMessage("Legacy query")
                .hasLineStart(5)
                .hasLineEnd(5);
        assertThat(issue.getDescription())
                .contains("Description: This is a test")
                .contains("Query URL: https://example.com/query")
                .contains("Platform: Terraform")
                .contains("Cloud provider: AWS")
                .contains("Category: Security")
                .contains("CWE: 79")
                .contains("Risk score: 7.5")
                .contains("Resource type: aws_resource")
                .contains("Resource name: my_resource")
                .contains("Issue type: MissingAttribute")
                .contains("Search key: prop")
                .contains("Search value: value")
                .contains("Expected value: secure")
                .contains("Actual value: insecure")
                .contains("Search line: 7");
    }

    @Test
    void shouldHandleNullDescriptionValue() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Query with null description",
                      "query_id": "test-002",
                      "files": [
                        {
                          "file_name": "test.yaml",
                          "line": 20
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .doesNotContain("Description: null");
    }

    @Test
    void shouldHandleBlankStringValues() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Query with blank values",
                      "query_id": "test-003",
                      "platform": "   ",
                      "files": [
                        {
                          "file_name": "test.yaml",
                          "line": 30,
                          "resource_name": "  "
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .doesNotContain("Platform:")
                .doesNotContain("Resource name:");
    }

    @Test
    void shouldUseStartLineAndEndLineFields() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Multi-line issue",
                      "query_id": "test-004",
                      "files": [
                        {
                          "file_name": "block.tf",
                          "start_line": 10,
                          "end_line": 25
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasLineStart(10)
                .hasLineEnd(25);
    }

    @Test
    void shouldPreferLineOverStartLine() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Line preference test",
                      "query_id": "test-005",
                      "files": [
                        {
                          "file_name": "test.yaml",
                          "line": 42,
                          "start_line": 10
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasLineStart(42)
                .hasLineEnd(42);
    }

    @Test
    void shouldHandleAllSeverities() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Critical issue",
                      "query_id": "crit-001",
                      "severity": "CRITICAL",
                      "files": [
                        {
                          "file_name": "critical.yaml",
                          "line": 1
                        }
                      ]
                    },
                    {
                      "query_name": "High issue",
                      "query_id": "high-001",
                      "severity": "HIGH",
                      "files": [
                        {
                          "file_name": "high.yaml",
                          "line": 2
                        }
                      ]
                    },
                    {
                      "query_name": "Medium issue",
                      "query_id": "med-001",
                      "severity": "MEDIUM",
                      "files": [
                        {
                          "file_name": "medium.yaml",
                          "line": 3
                        }
                      ]
                    },
                    {
                      "query_name": "Low issue",
                      "query_id": "low-001",
                      "severity": "LOW",
                      "files": [
                        {
                          "file_name": "low.yaml",
                          "line": 4
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(4);
        assertThat(report.get(0).getSeverity()).isEqualTo(Severity.ERROR);
        assertThat(report.get(1).getSeverity()).isEqualTo(Severity.WARNING_HIGH);
        assertThat(report.get(2).getSeverity()).isEqualTo(Severity.WARNING_NORMAL);
        assertThat(report.get(3).getSeverity()).isEqualTo(Severity.WARNING_LOW);
    }

    @Test
    void shouldSetCategoryWhenNotBlank() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "Categorized issue",
                      "query_id": "cat-001",
                      "platform": "Kubernetes",
                      "files": [
                        {
                          "file_name": "k8s.yaml",
                          "line": 1
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getCategory()).isEqualTo("Kubernetes");
    }

    @Test
    void shouldNotSetCategoryWhenBlank() {
        var report = parseStringContent("""
                {
                  "queries": [
                    {
                      "query_name": "No category issue",
                      "query_id": "nocat-001",
                      "files": [
                        {
                          "file_name": "test.yaml",
                          "line": 1
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getCategory()).isEmpty();
    }
}