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
}