package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link IntelephenseParser}.
 *
 * @author Akash Manna
 */
class IntelephenseParserTest extends AbstractParserTest {
    IntelephenseParserTest() {
        super("intelephense-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("src/Controller/HomeController.php")
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasColumnStart(15)
                .hasColumnEnd(19)
                .hasType("P1002")
                .hasMessage("Undefined variable: $user")
                .hasSeverity(Severity.ERROR)
                .hasCategory("intelephense");

        softly.assertThat(report.get(1))
                .hasFileName("src/Controller/HomeController.php")
                .hasLineStart(22)
                .hasLineEnd(22)
                .hasColumnStart(9)
                .hasColumnEnd(13)
                .hasType("P1003")
                .hasMessage("Unused variable $temp")
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("intelephense");

        softly.assertThat(report.get(2))
                .hasFileName("src/Service/AuthService.php")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasColumnStart(12)
                .hasColumnEnd(19)
                .hasType("P1004")
                .hasMessage("Possible null reference")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("intelephense");
    }

    @Override
    protected IssueParser createParser() {
        return new IntelephenseParser();
    }

    @Test
    void accepts() {
        assertThat(new IntelephenseParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("intelephense-report.json")))).isTrue();
        assertThat(new IntelephenseParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void shouldHandleUriFallback() {
        var filePath = FileSystems.getDefault().getPath("src", "FromUri.php");
        var report = parseStringContent("""
                [
                  {
                    "uri": "%s",
                    "diagnostics": [
                      {
                        "message": "URI fallback",
                        "severity": 2,
                        "code": "P2001",
                        "source": "intelephense",
                        "range": {
                          "start": {
                            "line": 0,
                            "character": 0
                          },
                          "end": {
                            "line": 0,
                            "character": 7
                          }
                        }
                      }
                    ]
                  }
                ]
                """.formatted(filePath.toUri()));

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
          .hasFileName(filePath.toAbsolutePath().toString().replace('\\', '/'))
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(1)
                .hasColumnEnd(7)
                .hasType("P2001")
                .hasMessage("URI fallback")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("intelephense");
    }

    @Test
    void shouldHandleMissingFields() {
        var report = parseStringContent("""
                {
                  "file_name": "src/Fallback.php",
                  "diagnostics": [
                    {
                      "message": "Fallback diagnostic"
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/Fallback.php")
                .hasType("-")
                .hasMessage("Fallback diagnostic")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("intelephense");

        assertThat(descriptor.getPattern()).isEqualTo("**/intelephense-report.json");
        assertThat(descriptor.getHelp()).contains("publishDiagnostics");
        assertThat(descriptor.getUrl()).isEqualTo("https://intelephense.com/");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}