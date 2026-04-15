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
 * Tests the class {@link CfnNagParser}.
 *
 * @author Akash Manna
 */
class CfnNagParserTest extends AbstractParserTest {
    CfnNagParserTest() {
        super("cfn-nag-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("templates/efs-encryption.yaml")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasType("F27")
                .hasMessage("EFS FileSystem should have encryption enabled")
                .hasDescription("Logical resource ID(s): EncryptedFileSystem")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(1))
                .hasFileName("templates/public-sg.yaml")
                .hasLineStart(19)
                .hasLineEnd(27)
                .hasType("W2")
                .hasMessage("Security Groups found with cidr open to world on ingress")
                .hasDescription("Logical resource ID(s): PublicAlbSecurityGroup, PublicAlbIngressRule")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("templates/public-sg.yaml")
                .hasLineStart(41)
                .hasLineEnd(41)
                .hasType("-")
                .hasMessage("Fallback rule id handling")
                .hasDescription("Logical resource ID(s): NoIdResource")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new CfnNagParser();
    }

    @Test
    void accepts() {
        var parser = new CfnNagParser();
        assertThat(parser.accepts(new FileReaderFactory(
                FileSystems.getDefault().getPath("cfn-nag-report.json")))).isTrue();
        assertThat(parser.accepts(new FileReaderFactory(
                FileSystems.getDefault().getPath("foo.txt")))).isFalse();
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
    void shouldHandleSingleFileObjectPayload() {
        var report = parseStringContent("""
                {
                  "filename": "single-template.yml",
                  "file_results": {
                    "failure_count": 1,
                    "violations": [
                      {
                        "id": "F16",
                        "type": "FAIL",
                        "message": "Single object payload",
                        "logical_resource_ids": [
                          "SingleResource"
                        ],
                        "line_numbers": [
                          8
                        ]
                      }
                    ]
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("single-template.yml")
                .hasType("F16")
                .hasSeverity(Severity.ERROR)
                .hasMessage("Single object payload")
                .hasLineStart(8)
                .hasLineEnd(8);
    }

    @Test
    void shouldHandleMissingViolationFields() {
        var report = parseStringContent("""
                [
                  {
                    "filename": "fallback.yml",
                    "file_results": {
                      "violations": [
                        {
                          "type": ""
                        }
                      ]
                    }
                  }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("fallback.yml")
                .hasType("-")
                .hasMessage("")
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldIgnoreNonObjectEntries() {
        var report = parseStringContent("""
                [
                  42,
                  {
                    "filename": "mixed.yml",
                    "file_results": {
                      "violations": [
                        "not-an-object",
                        {
                          "id": "W9",
                          "type": "WARN",
                          "message": "valid violation"
                        }
                      ]
                    }
                  }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("mixed.yml")
                .hasType("W9")
                .hasMessage("valid violation")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldIgnoreEntriesWithoutViolationsArray() {
        var report = parseStringContent("""
                [
                  {
                    "filename": "no-violations.yml",
                    "file_results": {
                      "failure_count": 1
                    }
                  }
                ]
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleEmptyLineAndResourceArrays() {
        var report = parseStringContent("""
                [
                  {
                    "filename": "arrays.yml",
                    "file_results": {
                      "violations": [
                        {
                          "id": "W1",
                          "type": "WARN",
                          "message": "empty arrays",
                          "line_numbers": [],
                          "logical_resource_ids": ["", "   "]
                        }
                      ]
                    }
                  }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("arrays.yml")
                .hasType("W1")
                .hasMessage("empty arrays")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasDescription("");
    }

    @Test
    void shouldHandleEmptyLogicalResourceIdsArray() {
        var report = parseStringContent("""
                [
                  {
                    "filename": "empty-resource-array.yml",
                    "file_results": {
                      "violations": [
                        {
                          "id": "W3",
                          "type": "WARN",
                          "message": "empty resources",
                          "logical_resource_ids": []
                        }
                      ]
                    }
                  }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("empty-resource-array.yml")
                .hasType("W3")
                .hasMessage("empty resources")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasDescription("");
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("cfn-nag");

        assertThat(descriptor.getPattern()).isEqualTo("**/cfn-nag-report.json");
        assertThat(descriptor.getHelp()).contains("cfn_nag_scan --input-path . --output-format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/stelligent/cfn_nag");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}