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
 * Tests the class {@link PsalmParser}.
 *
 * @author Akash Manna
 */
class PsalmParserTest extends AbstractParserTest {
    PsalmParserTest() {
        super("psalm-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("src/Service/UserService.php")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasColumnStart(15)
                .hasColumnEnd(33)
                .hasType("UndefinedMethod")
                .hasMessage("Method App\\Service\\UserService::findByUuid does not exist")
                .hasSeverity(Severity.ERROR)
                .hasCategory(DEFAULT_CATEGORY);

        softly.assertThat(report.get(1))
                .hasFileName("src/Domain/NotificationSender.php")
                .hasLineStart(33)
                .hasLineEnd(35)
                .hasColumnStart(19)
                .hasColumnEnd(27)
                .hasType("PossiblyNullArgument")
                .hasMessage("Argument 1 of App\\Domain\\Mailer::send cannot be null")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("src/Utils/Tokens.php")
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasColumnStart(9)
                .hasColumnEnd(23)
                .hasType("UnusedVariable")
                .hasMessage("$temporaryToken is never referenced or the value is not used")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Override
    protected IssueParser createParser() {
        return new PsalmParser();
    }

    @Test
    void accepts() {
        var parser = new PsalmParser();

        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("psalm-report.json"))))
                .isTrue();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("psalm-report.xml"))))
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
    void shouldHandleIssuesNestedByFiles() {
        var report = parseStringContent("""
                {
                    "files": {
                        "src/Fallback.php": {
                            "issues": [
                                {
                                    "severity": "warning",
                                    "line_from": 7,
                                    "line_to": 9,
                                    "column_from": 3,
                                    "column_to": 11,
                                    "type": "MixedAssignment",
                                    "message": "Cannot infer assignment type"
                                }
                            ]
                        }
                    }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/Fallback.php")
                .hasType("MixedAssignment")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(7)
                .hasLineEnd(9)
                .hasColumnStart(3)
                .hasColumnEnd(11);
    }

    @Test
    void shouldHandleMissingFields() {
        var report = parseStringContent("""
                {
                    "issues": [
                        {
                            "severity": "warning"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasMessage("-")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasFileName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasCategory(DEFAULT_CATEGORY);
    }

    @Test
    void shouldSkipNonObjectEntries() {
        var report = parseStringContent("""
                {
                    "issues": [
                        1,
                        "invalid",
                        {
                            "severity": "error",
                            "type": "InvalidScalarArgument",
                            "message": "Expected int but got string",
                            "file_name": "src/Parser.php"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("InvalidScalarArgument")
                .hasMessage("Expected int but got string")
                .hasFileName("src/Parser.php")
                .hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldSkipNonObjectFileEntries() {
        var report = parseStringContent("""
                {
                    "files": {
                        "src/ignored.php": "invalid-entry",
                        "src/Valid.php": {
                            "issues": [
                                {
                                    "severity": "warning",
                                    "type": "PossiblyFalseReference",
                                    "message": "Value can be false",
                                    "line_from": 4,
                                    "column_from": 8
                                }
                            ]
                        }
                    }
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/Valid.php")
                .hasType("PossiblyFalseReference")
                .hasMessage("Value can be false")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(4)
                .hasColumnStart(8);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("psalm");

        assertThat(descriptor.getPattern()).isEqualTo("**/psalm-report.json");
        assertThat(descriptor.getHelp()).contains("psalm --output-format=json");
        assertThat(descriptor.getUrl()).isEqualTo("https://psalm.dev/");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}