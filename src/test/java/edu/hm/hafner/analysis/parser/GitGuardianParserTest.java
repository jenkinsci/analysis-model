package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link GitGuardianParser}.
 *
 * @author Akash Manna
 */
class GitGuardianParserTest extends AbstractParserTest {
    GitGuardianParserTest() {
        super("gitguardian-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("src/app.py")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasColumnStart(9)
                .hasColumnEnd(44)
                .hasType("GitHub Token")
                .hasMessage("GitHub token detected")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(1))
                .hasFileName("infra/main.tf")
                .hasLineStart(27)
                .hasLineEnd(27)
                .hasColumnStart(15)
                .hasColumnEnd(35)
                .hasType("Hardcoded AWS Access Key")
                .hasMessage("Hardcoded AWS access key detected")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(2))
                .hasFileName("README.md")
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasType("Generic Password")
                .hasMessage("Secret detected")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldParseEdgeCases() {
        var report = parse("gitguardian-report-edge-cases.json");

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasFileName("config.yml")
                .hasLineStart(8)
                .hasColumnStart(1)
                .hasColumnEnd(25)
                .hasType("Slack Token")
                .hasMessage("xoxb-************************")
                .hasSeverity(Severity.WARNING_HIGH);

        assertThat(report.get(1))
                .hasFileName("scripts/deploy.sh")
                .hasType("Private Key")
                .hasMessage("Private key material found")
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("gitguardian");

        assertThat(descriptor.getType()).isEqualTo(IssueType.VULNERABILITY);
        assertThat(descriptor.getPattern()).isEqualTo("**/gitguardian-report.json");
        assertThat(descriptor.getHelp()).contains("ggshield secret scan path --recursive --json");
        assertThat(descriptor.getUrl()).isEqualTo("https://www.gitguardian.com/");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Test
    void shouldHandleFallbackBranchesAndNullEntries() {
        var report = parseStringContent("""
                {
                    "scanned_paths": [
                        null,
                        {
                            "path": "fallback-entry.txt",
                            "detector_name": "Inline Detector",
                            "message": "Inline finding"
                        },
                        {
                            "path": "fallback-policy.txt",
                            "policies": [
                                null,
                                {
                                    "name": "Inline Policy",
                                    "message": "Policy fallback finding",
                                    "location": {}
                                }
                            ]
                        },
                        {
                            "path": "fallback-secret.txt",
                            "secrets": [
                                null,
                                {
                                    "policy": "Inline Secret",
                                    "message": "Secret fallback finding"
                                }
                            ]
                        },
                        {
                            "path": "sparse-values.txt",
                            "incidents": [
                                null,
                                {}
                            ]
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(4);

        assertThat(report.get(0))
                .hasFileName("fallback-entry.txt")
                .hasType("Inline Detector")
                .hasMessage("Inline finding")
                .hasSeverity(Severity.WARNING_HIGH);

        assertThat(report.get(1))
                .hasFileName("fallback-policy.txt")
                .hasType("Inline Policy")
                .hasMessage("Policy fallback finding")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        assertThat(report.get(2))
                .hasFileName("fallback-secret.txt")
                .hasType("Inline Secret")
                .hasMessage("Secret fallback finding")
                .hasSeverity(Severity.WARNING_HIGH);

        assertThat(report.get(3))
                .hasFileName("sparse-values.txt")
                .hasType("-")
                .hasMessage("Secret detected")
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Test
    void shouldDetectDirectFindingsFromAllSupportedKeys() {
        var report = parseStringContent("""
                [
                    null,
                    {"line_start": 1},
                    {"line": 2},
                    {"match": "masked-secret-value"},
                    {"message": "message-driven-finding"},
                    {"detector_name": "Detector Based"},
                    {"policy": "Policy Based"},
                    {"type": "Type Based"},
                    {"id": "ID-123"},
                    {"severity": "high"},
                    {"confidence": "medium"}
                ]
                """);

        assertThat(report).hasSize(10);

        assertThat(report.get(0)).hasLineStart(1);
        assertThat(report.get(1)).hasLineStart(2);
        assertThat(report.get(2)).hasMessage("masked-secret-value");
        assertThat(report.get(3)).hasMessage("message-driven-finding");
        assertThat(report.get(4)).hasType("Detector Based");
        assertThat(report.get(5)).hasType("Policy Based");
        assertThat(report.get(6)).hasType("Type Based");
        assertThat(report.get(7)).hasType("ID-123");
        assertThat(report.get(8)).hasSeverity(Severity.WARNING_HIGH);
        assertThat(report.get(9)).hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldSupportTopLevelResultsAndSecretOccurrences() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "path": "results.json",
                            "matches": [
                                null,
                                {
                                    "detector": "Result Detector",
                                    "match": "result-level-secret",
                                    "location": {
                                        "start": {"line": 4, "column": 2},
                                        "end": {"line": 4, "column": 18}
                                    }
                                }
                            ],
                            "secrets": [
                                {
                                    "policy_name": "Results Policy",
                                    "occurrences": [
                                        null,
                                        {
                                            "line_end": 9,
                                            "end_column": 12
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasFileName("results.json")
                .hasType("Result Detector")
                .hasMessage("result-level-secret")
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasColumnStart(2)
                .hasColumnEnd(18);

        assertThat(report.get(1))
                .hasFileName("results.json")
                .hasType("Results Policy")
                .hasMessage("Secret detected")
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasColumnStart(12)
                .hasColumnEnd(12);
    }

    @Test
    void shouldIgnoreContainersWithoutFindings() {
        var report = parseStringContent("""
                {
                    "scanned_paths": [
                        {
                            "path": "only-path.txt"
                        },
                        {
                            "path": "empty-policies.txt",
                            "policies": []
                        },
                        {
                            "path": "empty-arrays.txt",
                            "matches": [],
                            "policy_breaks": [],
                            "incidents": [],
                            "secrets": []
                        },
                        {
                            "path": "policy-no-finding.txt",
                            "policies": [
                                {
                                    "name": "Policy Without Finding",
                                    "breaks": [],
                                    "incidents": []
                                }
                            ]
                        },
                        {
                            "path": "secret-no-finding.txt",
                            "secrets": [
                                {
                                    "policy_name": "Secret Without Finding",
                                    "occurrences": [],
                                    "matches": []
                                }
                            ]
                        }
                    ]
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldIgnoreRootObjectWithoutFindingKeys() {
        var report = parseStringContent("""
                {
                    "scanned_paths": [],
                    "results": [],
                    "metadata": {
                        "version": "1"
                    }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void accepts() {
        assertThat(new GitGuardianParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("gitguardian-report.json")))).isTrue();
        assertThat(new GitGuardianParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("gitguardian-report.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Override
    protected IssueParser createParser() {
        return new GitGuardianParser();
    }
}
