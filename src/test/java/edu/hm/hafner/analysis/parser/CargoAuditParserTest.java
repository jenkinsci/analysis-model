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
 * Tests the class {@link CargoAuditParser}.
 *
 * @author Akash Manna
 */
class CargoAuditParserTest extends AbstractParserTest {
    CargoAuditParserTest() {
        super("cargo-audit.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("chrono")
                .hasSeverity(Severity.ERROR)
                .hasType("RUSTSEC-2021-0079")
                .hasMessage("Buffer overflow in chrono before 0.4.19");

        softly.assertThat(report.get(0).getDescription())
                .contains("Buffer overflow in chrono")
                .contains("CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H")
                .contains("https://rustsec.org/advisories/RUSTSEC-2021-0079")
                .contains("2021-07-05");

        softly.assertThat(report.get(1))
                .hasFileName("sodiumoxide")
                .hasSeverity(Severity.ERROR)
                .hasType("RUSTSEC-2020-0001")
                .hasMessage("Cryptographic failure in sodiumoxide before 0.2.6");

        softly.assertThat(report.get(1).getDescription())
                .contains("Cryptographic failure in sodiumoxide")
                .contains("https://rustsec.org/advisories/RUSTSEC-2020-0001")
                .contains("2020-01-01");

        softly.assertThat(report.get(2))
                .hasFileName("rusqlite")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("RUSTSEC-2021-0120")
                .hasMessage("SQL injection in rusqlite before 0.25");

        softly.assertThat(report.get(2).getDescription())
                .contains("SQL injection in rusqlite")
                .contains("https://rustsec.org/advisories/RUSTSEC-2021-0120")
                .contains("2021-10-15");
    }

    @Override
    protected IssueParser createParser() {
        return new CargoAuditParser();
    }

    @Test
    void accepts() {
        assertThat(new CargoAuditParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("cargo-audit.json")))).isTrue();
        assertThat(new CargoAuditParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyVulnerabilities() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {},
                  "meta": {}
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingAdvisory() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "versions": {
                        "patched": ["1.0"],
                        "unaffected": []
                      }
                    }
                  },
                  "meta": {}
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldParseWithOptionalFields() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability"
                      },
                      "versions": {
                        "patched": ["2.0"],
                        "unaffected": ["1.5"]
                      }
                    }
                  },
                  "meta": {}
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("test-package")
                .hasType("RUSTSEC-2021-9999")
                .hasMessage("Test vulnerability");
    }

    @Test
    void shouldHandleNoVulnerabilitiesKey() {
        var report = parseStringContent("""
                {
                  "meta": {
                    "advisory_db": "https://github.com/rustsec/advisory-db"
                  }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldMapSeverityNullToWarningNormal() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldMapSeverityLowToWarningLow() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability",
                        "severity": "low"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldMapSeverityMediumToWarningNormal() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability",
                        "severity": "medium"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldMapSeverityHighToError() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability",
                        "severity": "high"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldMapSeverityCriticalToError() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability",
                        "severity": "critical"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldMapUnknownSeverityToWarningNormal() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability",
                        "severity": "unknown-severity"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldHandleEmptyTitleAndDescription() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "",
                        "description": "",
                        "severity": "high"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getMessage()).isEmpty();
        assertThat(report.get(0).getDescription()).isEmpty();
    }

    @Test
    void shouldHandleOptionalFieldsNotIncludedInDescription() {
        var reportNoCvss = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability",
                        "severity": "high",
                        "cvss": ""
                      }
                    }
                  }
                }
                """);
        assertThat(reportNoCvss.get(0).getDescription()).doesNotContain("CVSS:");

        var reportNoUrl = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability",
                        "severity": "high",
                        "url": ""
                      }
                    }
                  }
                }
                """);
        assertThat(reportNoUrl.get(0).getDescription()).doesNotContain("Reference:");

        var reportNoDate = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability",
                        "severity": "high",
                        "date": ""
                      }
                    }
                  }
                }
                """);
        assertThat(reportNoDate.get(0).getDescription()).doesNotContain("Published:");
    }

    @Test
    void shouldHandleAllOptionalFieldsPresent() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability",
                        "description": "Test description",
                        "severity": "high",
                        "cvss": "CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H",
                        "url": "https://rustsec.org/test",
                        "date": "2021-05-07"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        var description = report.get(0).getDescription();
        assertThat(description)
                .contains("Test vulnerability")
                .contains("Test description")
                .contains("CVSS:")
                .contains("Reference:")
                .contains("https://rustsec.org/test")
                .contains("Published:")
                .contains("2021-05-07");
    }

    @Test
    void shouldHandleDefaultPackageNameWhenMissing() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "title": "Test vulnerability",
                        "severity": "high"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getFileName()).isEqualTo("unknown");
    }

    @Test
    void shouldHandleDefaultIdWhenMissing() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "package": "test-package",
                        "title": "Test vulnerability",
                        "severity": "high"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getType()).isEqualTo("-");
    }

    @Test
    void shouldHandleMultipleVulnerabilities() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-0001",
                        "package": "package1",
                        "title": "Vulnerability 1",
                        "severity": "critical"
                      }
                    },
                    "1": {
                      "advisory": {
                        "id": "RUSTSEC-2021-0002",
                        "package": "package2",
                        "title": "Vulnerability 2",
                        "severity": "low"
                      }
                    },
                    "2": {
                      "advisory": {
                        "id": "RUSTSEC-2021-0003",
                        "package": "package3",
                        "title": "Vulnerability 3",
                        "severity": "medium"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(3);
        assertThat(report.get(0)).hasSeverity(Severity.ERROR);
        assertThat(report.get(1)).hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(2)).hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldSkipVulnerabilitiesWithoutAdvisory() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-0001",
                        "package": "package1",
                        "title": "Valid vulnerability",
                        "severity": "high"
                      }
                    },
                    "1": {
                      "versions": {
                        "patched": ["2.0"]
                      }
                    },
                    "2": {
                      "advisory": {
                        "id": "RUSTSEC-2021-0003",
                        "package": "package3",
                        "title": "Another valid vulnerability",
                        "severity": "medium"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(2);
        assertThat(report.get(0).getType()).isEqualTo("RUSTSEC-2021-0001");
        assertThat(report.get(1).getType()).isEqualTo("RUSTSEC-2021-0003");
    }

    @Test
    void shouldMapMissingSeverityToWarningNormal() {
        var report = parseStringContent("""
                {
                  "vulnerabilities": {
                    "0": {
                      "advisory": {
                        "id": "RUSTSEC-2021-9999",
                        "package": "test-package",
                        "title": "Test vulnerability"
                      }
                    }
                  }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);
    }
}
